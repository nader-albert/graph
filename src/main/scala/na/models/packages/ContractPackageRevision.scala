package na.models.packages

import java.time.LocalDateTime

import na.models.{Document, Entity, Person, Versioned}

case class ContractPackageRevision(override val uuid: Long, override val name: String, override val version: Int,
                                   contractPackage: ContractPackage, createdAt: Option[LocalDateTime], createdBy: Option[Person],
                                   documents: Seq[Document])
    extends Entity with Versioned {

}

object ContractPackageRevision {
    /**
      * the name of a table in the relational model
      * the name of a label in the graph model
      * */
    val typeName = "Package_Revision"

    def apply(uuid: Long, name: String, version: Int, contractPackage: ContractPackage): ContractPackageRevision = {
        ContractPackageRevision(uuid, name, version, contractPackage, None, None, Seq.empty[Document])
    }

    def apply(uuid: Long, name: String, version: Int, contractPackage: ContractPackage, documents: Seq[Document]): ContractPackageRevision = {
        ContractPackageRevision(uuid, name, version, contractPackage, None, None, documents)
    }
}
