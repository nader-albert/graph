package na.models.packages

import java.time.LocalDateTime

import na.models.documents.DocumentRevision
import na.models.{Entity, Person, Versioned}

case class ContractPackageRevision(override val uuid: Long, override val name: String, override val version: Int,
                                   contractPackage: ContractPackage, createdAt: Option[LocalDateTime], createdBy: Option[Person],
                                   documents: Seq[DocumentRevision])
    extends Entity with Versioned {

}

object ContractPackageRevision {
    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val label = "Package_Revision"
    val alias = "pkRevision"

    def apply(uuid: Long, name: String, contractPackage: ContractPackage): ContractPackageRevision = {
        ContractPackageRevision(uuid, name, 0, contractPackage, None, None, Seq.empty[DocumentRevision])
    }

    def apply(uuid: Long, name: String, version: Int, contractPackage: ContractPackage): ContractPackageRevision = {
        ContractPackageRevision(uuid, name, version, contractPackage, None, None, Seq.empty[DocumentRevision])
    }

    def apply(uuid: Long, name: String, version: Int, contractPackage: ContractPackage, documents: Seq[DocumentRevision]): ContractPackageRevision = {
        ContractPackageRevision(uuid, name, version, contractPackage, None, None, documents)
    }
}
