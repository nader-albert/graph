package na.models.documents

case class Document(override val uuid: Long, override val name: String) extends DocumentTemplate

object Document {
    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Document_Template"
    val alias = "document"
}
