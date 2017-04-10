package na.models

import java.time.LocalDateTime

case class ContractRevision private(override val id: Long, override val name: String, override val version: Int, documentsPackage: Package,
                                    signed: Boolean, signDate: Option[LocalDateTime], signedBy: Option[Candidate],
                                    title: Option[String], description: Option[String])

    extends Entity with Versioned {


}

object ContractRevision {

    /**
      * to be used by graph repositories
      * */
    def apply(id: Long, name: String, version: Int, documentsPackage: Package): ContractRevision = {
        ContractRevision(id, name, version, documentsPackage, signed= false, signDate = None, signedBy = None, title = None, description = None)
    }

  /**
    * used by relational repositories
    */
    def apply(contract: ContractRevision, signed: Boolean, signDate: LocalDateTime, signedBy: Candidate, title: String, description: String): ContractRevision = {
        contract.copy(signed = signed, signDate = Some(signDate), signedBy = Some(signedBy), title = Some(title), description = Some(description))
    }
}
