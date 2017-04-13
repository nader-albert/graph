package na.models.documents

import java.time.LocalDateTime

import na.models.sections.SectionRevision
import na.models.{Entity, Person, Versioned}

case class DocumentRevision private(override val uuid: Long,
                                    override val name: String,
                                    override val version: Int,
                                    document: Document,
                                    sections: Seq[SectionRevision],
                                    createdAt: Option[LocalDateTime], createdBy: Option[Person])

    extends Entity with Versioned {
}

object DocumentRevision {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Document_Revision"
    val alias: String = "dcRevision"

    /**
      * */
    def apply(uuid: Long, name: String, version: Int, document: Document): DocumentRevision = {
        DocumentRevision(uuid, name, version, document, Seq.empty[SectionRevision], None, None)
    }

    def apply(uuid: Long, name: String, version: Int, document: Document, sections: Seq[SectionRevision]): DocumentRevision = {
        DocumentRevision(uuid, name, version, document, sections, None, None)
    }
}
