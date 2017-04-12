package na.models.packages

import na.models.Template

/***
  * in relational model, where package templates are supposed to be enlisted
  * in graph model, this represents a label, that should be assigned to all contract template nodes
  * */
trait PackageTemplate extends Template {
    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val typeName = "Package_Template"
}
