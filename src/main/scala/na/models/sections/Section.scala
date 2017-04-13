package na.models.sections

case class Section (uuid: Long, name: String) extends SectionTemplate

object Section {
    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Section_Template"
    val alias = "section"
}
