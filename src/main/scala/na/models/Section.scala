package na.models

case class Section (id: Long, name: String, override val version: Int)
    extends Entity with Versioning {

}
