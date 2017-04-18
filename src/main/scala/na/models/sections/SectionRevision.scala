package na.models.sections

import java.time.LocalDateTime

import na.models.{Entity, Person, Versioned}

case class SectionRevision private (override val uuid: Long,
                               override val name: String,
                               override val version: Int,
                               section: Section,
                               createdAt: Option[LocalDateTime],
                               createdBy: Option[Person])
    extends Entity with Versioned {

}

object SectionRevision {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Section_Revision"
    val alias = "scRevision"

    /**
      * */
    def apply(uuid: Long, name: String, section: Section): SectionRevision = {
        SectionRevision(uuid, name, 0, section, None, None)
    }

    /**
      * */
    def apply(uuid: Long, name: String, version: Int, section: Section): SectionRevision = {
        SectionRevision(uuid, name, version, section, None, None)
    }
}