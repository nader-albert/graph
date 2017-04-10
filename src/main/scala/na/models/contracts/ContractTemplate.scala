package na.models.contracts

import na.models.Template

/***
  * in relational model, where contract templates are supposed to be enlisted
  * in graph model, this represents a label, that should be assigned to all contract template nodes
  * */
trait ContractTemplate extends Template {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val name = "Contract_Template"

}
