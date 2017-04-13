package na.models.packages

case class ContractPackage(override val uuid: Long, override val name: String) extends PackageTemplate

object ContractPackage {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Package_Template"
    val alias = "package"
}