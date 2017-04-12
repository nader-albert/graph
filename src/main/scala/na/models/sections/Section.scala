package na.models.sections

import na.models.{Entity, Versioned}

case class Section (uuid: Long, name: String, override val version: Int)
    extends Entity with Versioned {

}
