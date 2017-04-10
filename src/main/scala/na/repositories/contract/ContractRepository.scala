package na.repositories.contract

import na.models.contracts.{Contract, ContractRevision}
import na.models.neo4j.RelTypes
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

    /**
      * creates a new template from the specified type
      * */
    override def create(contract: Contract): Contract = {
        val alias = contract.name
        val contractLabel = contract.typeName
        val contractId = "id: {id}"
        val contractName = "name: {name}"

        //"CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King")

        executeInTransaction {
            ("CREATE (" + alias + ":" + contractLabel + " {" + contractId + ", " + contractName + " })"
                , parameters("id", contract.id.toString , "name", contract.name) )
        }

        contract
    }

    /**
      * creates a new version and link it with the given template
      * */
    override def create(contract: Contract, revision: ContractRevision): ContractRevision = {
        val contractRevision = addRevision(revision)

        "(" + contract.name + ")" + "-[:" + RelTypes.HAS_A + "]->" + "(" + contractRevision + ")"

        executeInTransaction {
            ("CREATE ({contract})-[:{relation}->({revision}))",
                parameters("contract", contract.name, "relation", RelTypes.HAS_A, "revision", contractRevision.name))
        }

        contractRevision
    }

    private def addRevision(revision: ContractRevision): ContractRevision = {
        val alias = revision.name
        val revisionLabel = ContractRevision.typeName
        val contractId = "id: {id}"
        val contractName = "name: {name}"

        //create revision
        executeInTransaction {
            ("CREATE (" + alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " })"
                , parameters("id", revision.id.toString , "name", revision.name) )
        }

        revision
    }

    /**
      * Finds an entity corresponding to the one passed in the parameter. basically
      * */
    /*def find(template: Contract): ContractRevision = {
        executeInTransaction {
            ("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
        }
    }*/
}
