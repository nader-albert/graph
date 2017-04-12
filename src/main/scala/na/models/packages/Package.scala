package na.models.packages

case class Package(override val uuid: Long, override val name: String) extends PackageTemplate
