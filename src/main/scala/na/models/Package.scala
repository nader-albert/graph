package na.models

case class Package(id: Long, name: String, override val version: Int, documents: Seq[Document])
    extends Entity with Versioning {

}
