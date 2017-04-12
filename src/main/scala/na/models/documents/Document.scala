package na.models.documents

import na.models.{Entity, Section, Versioned}

case class Document(override val uuid: Long, override val name: String, override val version: Int, sections: Seq[Section])
    extends Entity with Versioned {

}
