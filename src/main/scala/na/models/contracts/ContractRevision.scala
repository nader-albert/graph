package na.models.contracts

import java.time.LocalDateTime

import na.models.packages.ContractPackage
import na.models.{Candidate, Entity, Versioned}

case class ContractRevision private(override val uuid: Long, override val name: String, override val version: Int, contract: Contract,
                                    documentsPackage: Option[ContractPackage],
                                    signed: Boolean, signDate: Option[LocalDateTime], signedBy: Option[Candidate],
                                    title: Option[String], description: Option[String])

    extends Entity with Versioned {

}

object ContractRevision {

    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val typeName = "Contract_Revision"

    /**
      * */
    def apply(uuid: Long, name: String, version: Int, contract: Contract): ContractRevision = {
        ContractRevision(uuid, name, version, contract, None, signed= false, signDate = None, signedBy = None, title = None, description = None)
    }

    /**
      * to be used by graph repositories
      * */
    def apply(uuid: Long, name: String, version: Int, contract: Contract, contractPackage: ContractPackage): ContractRevision = {
        ContractRevision(uuid, name, version, contract, Some(contractPackage), signed= false, signDate = None, signedBy = None, title = None, description = None)
    }

  /**
    * used by relational repositories
    */
    def apply(contract: ContractRevision, signed: Boolean, signDate: LocalDateTime, signedBy: Candidate, title: String, description: String): ContractRevision = {
        contract.copy(signed = signed, signDate = Some(signDate), signedBy = Some(signedBy), title = Some(title), description = Some(description))
    }
}
