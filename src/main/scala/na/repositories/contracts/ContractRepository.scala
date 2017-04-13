package na.repositories.contracts

import na.models.contracts.{Contract, ContractRevision}
import na.models.neo4j.RelTypes
import na.models.packages.ContractPackageRevision
import na.repositories.packages.ContractPackageRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters
import scala.collection.JavaConversions._

object ContractRepository extends RelationalRepository[ContractRevision] with GraphRepository[Contract, ContractRevision] {

    /*def findInGDM(contract: => Contract): Contract = {
        //model with basic attributes and all relations from graph
       // find
        //TODO: access graph driver here
        //Contract()
      ???
    }

    def findInRDM (id: Long): Contract = ???*/

    /***
      * */
    override def add(contract: Contract): Unit = {
        val alias = contract.name
        val contractLabel = Contract.label
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            (
                CREATE (alias + ":" + contractLabel + " {" + contractId + ", " + contractName + "}")
                ,
                parameters("uuid", contract.uuid.toString , "name", contract.name)
            )
        }
    }

    /***
      * */
    override def add(revision: ContractRevision): Unit = {
        val alias = revision.name
        val revisionLabel = ContractRevision.label
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            (
                CREATE (alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " }")
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name)
            )
        }
    }

    override def attach(contract: Contract, revision: ContractRevision): ContractRevision = {
        execute {
            (
                MATCH(one(contract) and currentOne(revision))
                    andThen
                CREATE(leftLink(Contract.alias, RelTypes.HAS_A.name(), ContractRevision.alias))
                ,
                parameters(
                    "contractName", contract.name,
                    "contractUuid", contract.uuid.toString,
                    "ctCurrentRevisionUuid", revision.uuid.toString,
                    "ctCurrentRevisionName", revision.name)
            )
        }

        revision
    }

    override def attach(currentRevision: ContractRevision, nextRevision: ContractRevision): ContractRevision = {
        execute {
            (
                MATCH(currentOne(currentRevision) and nextOne(nextRevision))
                    andThen
                CREATE(leftLink(ContractRevision.alias, RelTypes.NEXT.name(), ContractRevision.alias + "next"))
                    andThen
                CREATE(rightLink(ContractRevision.alias + "next", RelTypes.PREVIOUS.name(), ContractRevision.alias))
                ,
                parameters(
                    "ctCurrentRevisionName", currentRevision.name,
                    "ctCurrentRevisionUuid", currentRevision.uuid.toString,
                    "ctNextRevisionUuid", nextRevision.uuid.toString,
                    "ctNextRevisionName", nextRevision.name)
            )
        }

        nextRevision
    }

    /***
     * @param contractRevision, the contract revision required to be connected to a package revision
     * @param contractPackageRevision, the package revision required to be connected to a contract revision
     * @return a contract revision linked to the given package revision
     * */
    def attach(contractRevision: ContractRevision, contractPackageRevision: ContractPackageRevision): Unit = {
        execute {
            (
                MATCH(currentOne(contractRevision) and ContractPackageRepository.one(contractPackageRevision))
                    andThen
                CREATE(leftLink(ContractRevision.alias, RelTypes.CONTAINS_A.name(), ContractPackageRevision.alias))
                ,
                parameters(
                    "ctCurrentRevisionName", contractRevision.name,
                    "ctCurrentRevisionUuid", contractRevision.uuid.toString,
                    "pkRevisionName", contractPackageRevision.name,
                    "pkRevisionUuid", contractPackageRevision.uuid.toString)
            )
        }
    }

    override def find(contract: Contract, connectionName: String): Option[ContractRevision] = {
        execute {
            (
                MATCH(one(contract) thatIs connectedTo(connectionName, ContractRevision.alias))
                    andThen
                RETURN ("%s, %s, %s".format(Contract.alias, "r", ContractRevision.alias))
                ,
                parameters(
                    "contractName", contract.name,
                    "contractUuid", contract.uuid.toString
                )
            )
        }
            .filter{result => result.hasNext}
            .map(result => result.next())
            .map{record => ContractRevision(record.get(ContractRevision.alias).get("uuid").asString().toLong, record.get(ContractRevision.alias).get("name").asString(), contract)}

    }

    override def find(revision: ContractRevision, connectionName: String): Option[ContractRevision] = {
        execute {
            (
                MATCH(currentOne(revision) thatIs connectedTo(connectionName, ContractRevision.alias+"suffix"))
                    andThen
                RETURN ("%s, %s".format(ContractRevision.alias+"suffix", "r"))
                ,
                parameters(
                    "ctCurrentRevisionName", revision.name,
                    "ctCurrentRevisionUuid", revision.uuid.toString
                )
            )
        }
            .filter{result => result.hasNext}
            .map(result => result.list().reverse.last) //TODO: Check if it should really remain reverse or get it back... for some reason I noticed that it doesn't matter ... which of course doesnt make any sense
            .map(record => ContractRevision(record.get(ContractRevision.alias+"suffix").get("uuid").asString().toLong, record.get(ContractRevision.alias+"suffix").get("name").asString(), revision.contract))
    }

    def one(contract :Contract): String =
        "(%s:%s {name:{contractName}, uuid:{contractUuid} } )"
            .format (Contract.alias, Contract.label)

    def currentOne(contractRevision: ContractRevision): String =
        "(%s:%s {name:{ctCurrentRevisionName}, uuid:{ctCurrentRevisionUuid} } )"
            .format(ContractRevision.alias, ContractRevision.label)

    def nextOne(contractRevision: ContractRevision): String =
        "(%s:%s {name:{ctNextRevisionName}, uuid:{ctNextRevisionUuid} } )"
            .format(ContractRevision.alias+"next", ContractRevision.label) //To differentiate aliases
}

