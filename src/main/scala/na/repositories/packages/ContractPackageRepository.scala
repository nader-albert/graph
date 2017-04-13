package na.repositories.packages

import na.models.documents.DocumentRevision
import na.models.neo4j.RelTypes
import na.models.packages.{ContractPackage, ContractPackageRevision}
import na.repositories.documents.DocumentRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

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
                CREATE(leftLink(ContractPackage.alias, RelTypes.HAS_A.name(), ContractPackageRevision.alias)),

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
                MATCH(one(contractPackageRevision) and DocumentRepository.one(documentRevision))
                    andThen
                CREATE(leftLink(ContractPackageRevision.alias, RelTypes.CONTAINS_A.name(), DocumentRevision.alias))
                ,
                parameters(
                    "pkRevisionName", contractPackageRevision.name,
                    "pkRevisionUuid", contractPackageRevision.uuid.toString,
                    "dcRevisionName", documentRevision.name,
                    "dcRevisionUuid", documentRevision.uuid.toString)
            )
        }
    }

    def one(contract :ContractPackage): String =
        "(%s:%s {name:{packageName}, uuid:{packageUuid} } )"
            .format(ContractPackage.alias, ContractPackage.label)

    def one(contractRevision: ContractPackageRevision): String =
        "(%s:%s {name:{pkRevisionName}, uuid:{pkRevisionUuid} } )"
            .format(ContractPackageRevision.alias, ContractPackageRevision.label)

    override def attach(previousRevision: ContractPackageRevision, nextRevision: ContractPackageRevision): ContractPackageRevision = ???

    override def find(template: ContractPackage, connectionName: String): Option[ContractPackageRevision] = ???

    override def find(currentRevision: ContractPackageRevision, connectionName: String): Option[ContractPackageRevision] = ???
}
