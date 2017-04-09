package na.repositories.contract

import na.models.Contract
import na.repositories.{GraphRepository, RelationalRepository}

object ContractRepository extends RelationalRepository[Contract] with GraphRepository[Contract] {

    def findInGDM(contract: => Contract): Contract = {
        //model with basic attributes and all relations from graph
       // find
        //TODO: access graph driver here
        //Contract()
      ???
    }

    def findInRDM (id: Long): Contract = ???
}
