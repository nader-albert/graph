package na.models

import java.time.LocalDateTime

case class Contract private (id: Long, name: String, override val version: Int, documentsPackage: Package,
                             signed: Boolean, signDate: Option[LocalDateTime], signedBy: Option[Candidate],
                             title: Option[String], description: Option[String])

    extends Entity with Versioning {


}

object Contract {

    def apply(id: Long, name: String, version: Int, documentsPackage: Package): Contract = {
        Contract(id, name, version, documentsPackage, signed= false, signDate = None, signedBy = None, title = None, description = None)
    }

    def apply(contract: Contract, signed: Boolean, signDate: LocalDateTime, signedBy: Candidate, title: String, description: String): Contract = {
        contract.copy(signed = signed, signDate = Some(signDate), signedBy = Some(signedBy), title = Some(title), description = Some(description))
    }
}
