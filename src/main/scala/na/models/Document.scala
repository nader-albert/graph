package na.models

case class Document(override val uuid: Long, override val name: String, override val version: Int, sections: Seq[Section])
    extends Entity with Versioned {

}
