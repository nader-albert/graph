package na.repositories.contracts

import na.models.contracts.{Contract, ContractRevision}
import na.models.neo4j.RelTypes
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

object ContractRepository extends RelationalRepository[ContractRevision] with GraphRepository[Contract, ContractRevision] {

    override val templateAlias = "contract"

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
    override def add(contract: Contract): Contract = {
        val alias = contract.name
        val contractLabel = contract.typeName
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + contractLabel + " {" + contractId + ", " + contractName + "})"
                , parameters("uuid", contract.uuid.toString , "name", contract.name) )
        }

        contract
    }

    /***
      * */
    override def add(revision: ContractRevision): ContractRevision = {
        val alias = revision.name
        val revisionLabel = ContractRevision.typeName
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " })"
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }

        revision
    }

    override def attach(contract: Contract, revision: ContractRevision): ContractRevision = {
        execute {
            (
                MATCH(one(contract) and one(revision))
                    andThen
                CREATE(link(RelTypes.HAS_A.name())) ,

                parameters(
                    "contractName", contract.name,
                    "contractUuid", contract.uuid.toString,
                    "revisionUuid", revision.uuid.toString,
                    "revisionName", revision.name))
        }

        revision
    }

    override def find(template: Contract): ContractRevision = ???

    /***
     // * @param contractRevision, the contract revision required to be connected to a package revision
     // * @param packageRevision, the package revision required to be connected to a contract revision
     //// * @return a contract revision linked to the given package revision
      * */
    //def attach(contractRevision: ContractRevision, packageRevision: PackageRevision): ContractRevision = {
    //}

    def one(contract :Contract): String = "(" + templateAlias + ":" + contract.typeName + "{name:{contractName}, uuid:{contractUuid} } )"

    def one(contractRevision: ContractRevision): String = "(" + reversionAlias + ":" + ContractRevision.typeName + "{name:{revisionName}, uuid:{revisionUuid} } )"

    def link(connectionName: String): String = "(" + templateAlias + ")-[r:" + connectionName + "]->(" + reversionAlias + ")"


    /**
      * Finds an entity corresponding to the one passed in the parameter. basically
      * */
    /*def find(template: Contract): ContractRevision = {
        executeInTransaction {
            ("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
        }
    }*/

}



