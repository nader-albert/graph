package na.models.packages

import java.time.LocalDateTime

import na.models.{Document, Person, Versioned}
import org.neo4j.graphdb.Entity

case class PackageRevision(override val id: Long, override val name: String, createdAt: Option[LocalDateTime], createdBy: Option[Person],
                   documents: Seq[Document], override val version: Int)
    extends Entity with Versioned {

}
