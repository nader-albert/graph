package na.models

import java.time.LocalDateTime

case class PackageRevision(override val id: Long, override val name: String, createdAt: Option[LocalDateTime], createdBy: Option[Person],
                   documents: Seq[Document], override val version: Int)
    extends Entity with Versioned {

}
