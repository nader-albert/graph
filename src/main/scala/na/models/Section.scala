package na.models

case class Section (uuid: Long, name: String, override val version: Int)
    extends Entity with Versioned {

}
