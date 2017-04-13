package na.repositories.contracts

import na.models.contracts.{Contract, ContractRevision}
import na.models.neo4j.RelTypes
import na.models.packages.ContractPackageRevision
import na.repositories.packages.ContractPackageRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

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
            ("CREATE (" + alias + ":" + contractLabel + " {" + contractId + ", " + contractName + "})"
                , parameters("uuid", contract.uuid.toString , "name", contract.name) )
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
            ("CREATE (" + alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " })"
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }
    }

    override def attach(contract: Contract, revision: ContractRevision): ContractRevision = {
        execute {
            (
                MATCH(one(contract) and one(revision))
                    andThen
                CREATE(link(Contract.alias, RelTypes.HAS_A.name(), ContractRevision.alias)) ,

                parameters(
                    "contractName", contract.name,
                    "contractUuid", contract.uuid.toString,
                    "ctRevisionUuid", revision.uuid.toString,
                    "ctRevisionName", revision.name))
        }

        revision
    }

    /***
     * @param contractRevision, the contract revision required to be connected to a package revision
     * @param contractPackageRevision, the package revision required to be connected to a contract revision
     * @return a contract revision linked to the given package revision
     * */
    def attach(contractRevision: ContractRevision, contractPackageRevision: ContractPackageRevision): Unit = {
        execute {
            (
                MATCH(one(contractRevision) and ContractPackageRepository.one(contractPackageRevision))
                    andThen
                CREATE(link(ContractRevision.alias, RelTypes.CONTAINS_A.name(), ContractPackageRevision.alias))
                ,
                parameters(
                    "ctRevisionName", contractRevision.name,
                    "ctRevisionUuid", contractRevision.uuid.toString,
                    "pkRevisionName", contractPackageRevision.name,
                    "pkRevisionUuid", contractPackageRevision.uuid.toString)
            )
        }
    }

    override def find(template: Contract): ContractRevision = ???

    def one(contract :Contract): String =
        "(%s:%s {name:{contractName}, uuid:{contractUuid} } )"
            .format (Contract.alias, Contract.label)

    def one(contractRevision: ContractRevision): String =
        "(%s:%s {name:{ctRevisionName}, uuid:{ctRevisionUuid} } )"
            .format(ContractRevision.alias, ContractRevision.label)

}



