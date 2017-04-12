package na.models.contracts

case class Contract(override val uuid: Long, override val name: String) extends ContractTemplate

