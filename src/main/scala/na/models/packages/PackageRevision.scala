package na.models.packages

import java.time.LocalDateTime

import na.models.{Document, Entity, Person, Versioned}

case class PackageRevision(override val id: Long, override val name: String, createdAt: Option[LocalDateTime], createdBy: Option[Person],
                   documents: Seq[Document], override val version: Int)
    extends Entity with Versioned {

}
