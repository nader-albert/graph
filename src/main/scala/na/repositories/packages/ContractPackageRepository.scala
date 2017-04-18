package na.repositories.packages

import na.models.documents.DocumentRevision
import na.models.neo4j.RelTypes
import na.models.packages.{ContractPackage, ContractPackageRevision}
import na.repositories.documents.DocumentRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

import scala.collection.JavaConversions._

object ContractPackageRepository extends RelationalRepository[ContractPackageRevision]
    with GraphRepository[ContractPackage, ContractPackageRevision]{

    /**
      * creates a new template from the specified type
      **/
    override def add(templatePackage: ContractPackage): Unit = {
        val alias = templatePackage.name
        val contractPackageLabel = ContractPackage.label
        val packageId = "uuid: {uuid}"
        val packageName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + contractPackageLabel + " {" + packageId + ", " + packageName + "})"
                , parameters("uuid", templatePackage.uuid.toString , "name", templatePackage.name) )
        }
    }

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: ContractPackageRevision): Unit = {
        val alias = revision.name
        val revisionLabel = ContractPackageRevision.label
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " })"
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }
    }

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(templatePackage: ContractPackage, revision: ContractPackageRevision): ContractPackageRevision = {
        execute {
            (
                MATCH(one(templatePackage) and one(revision))
                    andThen
                CREATE(leftToRightLink(ContractPackage.alias, RelTypes.HAS_A.name(), ContractPackageRevision.alias)),

                parameters(
                    "packageName", templatePackage.name,
                    "packageUuid", templatePackage.uuid.toString,
                    "pkRevisionUuid", revision.uuid.toString,
                    "pkRevisionName", revision.name))
        }

        revision
    }

    /***
      * @param contractPackageRevision, the package revision required to be connected to a document revision
      * @param documentRevision, the document revision required to be connected to a package revision
      * @return a contract revision linked to the given package revision
      * */
    def attach(contractPackageRevision: ContractPackageRevision, documentRevision: DocumentRevision): Unit = {
        execute {
            (
                MATCH(current(contractPackageRevision) and DocumentRepository.one(documentRevision))
                    andThen
                CREATE(leftToRightLink(ContractPackageRevision.alias, RelTypes.CONTAINS_A.name(), DocumentRevision.alias))
                ,
                parameters(
                    "pkCurrentRevisionName", contractPackageRevision.name,
                    "pkCurrentRevisionUuid", contractPackageRevision.uuid.toString,
                    "dcRevisionName", documentRevision.name,
                    "dcRevisionUuid", documentRevision.uuid.toString)
            )
        }
    }

    override def attach(currentRevision: ContractPackageRevision, nextRevision: ContractPackageRevision): ContractPackageRevision = {
        execute {
            (
                MATCH(current(currentRevision) and next(nextRevision))
                    andThen
                CREATE(leftToRightLink(ContractPackageRevision.alias, RelTypes.NEXT.name(), ContractPackageRevision.alias + "next"))
                    andThen
                CREATE(rightToLeftLink(ContractPackageRevision.alias, RelTypes.PREVIOUS.name(), ContractPackageRevision.alias + "next"))
                ,
                parameters(
                    "pkCurrentRevisionName", currentRevision.name,
                    "pkCurrentRevisionUuid", currentRevision.uuid.toString,
                    "pkNextRevisionUuid", nextRevision.uuid.toString,
                    "pkNextRevisionName", nextRevision.name)
            )
        }

        nextRevision
    }

    override def find(contractPackage: ContractPackage, connectionName: String): Option[ContractPackageRevision] = {
        execute {
            (
                MATCH(one(contractPackage) thatIs connectedTo(connectionName, ContractPackageRevision.alias))
                    andThen
                RETURN ("%s, %s, %s".format(ContractPackage.alias, "r", ContractPackageRevision.alias))
                ,
                parameters(
                    "packageName", contractPackage.name,
                    "packageUuid", contractPackage.uuid.toString
                )
            )
        }.filter {result => result.hasNext}
            .map(result => result.next())
            .map {
                record => ContractPackageRevision(
                    record.get(ContractPackageRevision.alias).get("uuid").asString().toLong,
                    record.get(ContractPackageRevision.alias).get("name").asString(),
                    contractPackage)
            }
    }

    override def find(revision: ContractPackageRevision, connectionName: String): Option[ContractPackageRevision] = {
        execute {
            (
                MATCH(current(revision) thatIs connectedTo(connectionName, ContractPackageRevision.alias + "suffix"))
                    andThen
                    RETURN ("%s, %s".format(ContractPackageRevision.alias + "suffix", "r"))
                ,
                parameters(
                    "pkCurrentRevisionName", revision.name,
                    "pkCurrentRevisionUuid", revision.uuid.toString
                )
            )
        } //TODO: Could be done better by specifying the end of the path in the query itself instead of fetching the last record in the result !
            .filter{result => result.hasNext}
            .map(result => result.list().reverse.last) //TODO: Check if it should really remain reverse or get it back... for some reason I noticed that it doesn't matter ... which of course doesnt make any sense
            .map { record =>
                ContractPackageRevision(
                    record.get(ContractPackageRevision.alias+"suffix").get("uuid").asString().toLong,
                    record.get(ContractPackageRevision.alias+"suffix").get("name").asString(),
                    revision.contractPackage)
            }
    }

    def one(contract :ContractPackage): String =
        "(%s:%s {name:{packageName}, uuid:{packageUuid} } )"
            .format(ContractPackage.alias, ContractPackage.label)

   def one(contractRevision: ContractPackageRevision): String =
        "(%s:%s {name:{pkRevisionName}, uuid:{pkRevisionUuid} } )"
            .format(ContractPackageRevision.alias, ContractPackageRevision.label)

    def current(contractPackageRevision: ContractPackageRevision): String =
        "(%s:%s {name:{pkCurrentRevisionName}, uuid:{pkCurrentRevisionUuid} } )"
            .format(ContractPackageRevision.alias, ContractPackageRevision.label)

    def next(contractPackageRevision: ContractPackageRevision): String =
        "(%s:%s {name:{pkNextRevisionName}, uuid:{pkNextRevisionUuid} } )"
            .format(ContractPackageRevision.alias + "next", ContractPackageRevision.label) //To differentiate between aliases used in current and next
}
