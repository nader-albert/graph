package na.repositories.contract

import na.models.{ContractRevision, Contract}
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
    def create(template: Contract): Contract = {
        withTransaction {
            ("CREATE (a:C {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
        }
    }

    /**
      * creates a new version and link it with the given template
      * */
    def create(template: Contract, entity: ContractRevision): ContractRevision = {
        withTransaction {
            ("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
        }
    }

    /**
      * Finds an entity corresponding to the one passed in the parameter. basically
      * */
    def find(template: Contract): ContractRevision = {
        withTransaction {
            ("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
        }
    }
}
