package na.models.contracts

case class Contract(override val uuid: Long, override val name: String) extends ContractTemplate

object Contract {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Contract_Template"
    val alias = "contract"
}