package na

import na.models.neo4j.RelTypes
import org.neo4j.graphdb.{GraphDatabaseService, Label, Node, Relationship}
import Label.label
import na.models.contracts.{Contract, ContractRevision}
import na.models.documents.{Document, DocumentRevision}
import na.models.packages.{ContractPackage, ContractPackageRevision}
import na.models.sections.{Section, SectionRevision}

class ContractPool(graphDb: GraphDatabaseService) {

    var nodes = Map.empty[Node, (Relationship, Node)]

    withTransaction {
        val firstNode = graphDb.createNode
        val secondNode = graphDb.createNode

        firstNode.addLabel(label("CONTRACT_TEMPLATE"))
        firstNode.setProperty("name", "CT2")

        secondNode.addLabel(label("PACKAGE_TEMPLATE"))
        secondNode.setProperty("name", "PT2")

        val relationship = firstNode.createRelationshipTo(secondNode, RelTypes.LINKED_TO)

        nodes = nodes.updated(firstNode, (relationship, secondNode))
    }

    override def toString: String = {
        var k: String = ""

        withTransaction {
            k = nodes.map { node =>
                "node %s connected to %s via %s".format(node._1, node._2._2, node._2._1)
            }.fold("")((str1, str2) => str1 + "\n\r" + str2)

            graphDb.getAllLabels
        }

        k
    }

    def count: Any = {
        withTransaction {
            graphDb.findNodes(label("CONTRACT_TEMPLATE")).stream().count()
        }
    }

    private def withTransaction(expression: => Any): Any = try {
        val tx = graphDb.beginTx

        try {
            val result = expression
            tx.success()
            result
        } catch {
            case ex: Exception => tx.failure()
        } finally if (tx != null) tx.close()
    }
}

object ContractPool {

    def randomContracts: Seq[Contract] = {
        val contracts = Seq.empty[Contract]

        contracts
            .+:(Contract(uuid = 1, name = "CT_1"))
            .+:(Contract(uuid = 2, name = "CT_2"))
            .+:(Contract(uuid = 3, name = "CT_3"))
            .+:(Contract(uuid = 4, name = "CT_4"))
            .+:(Contract(uuid = 5, name = "CT_5"))
            .+:(Contract(uuid = 6, name = "CT_6"))
            .+:(Contract(uuid = 7, name = "CT_7"))
            .+:(Contract(uuid = 8, name = "CT_8"))
           // .+:(Contract(uuid = 9, name = "CT-9"))
           // .+:(Contract(uuid = 10,name = "CT10"))
    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    def randomContractRevisionsFor(contracts: Seq[Contract], contractPackageRevisions: Seq[ContractPackageRevision]): Seq[ContractRevision] = {
        val contractRevisions = Seq.empty[ContractRevision]

        val invertedContracts = contracts.reverse

        val CT1 = invertedContracts.head
        val CT2 = invertedContracts.drop(1).head
        val CT3 = invertedContracts.drop(2).head
        val CT4 = invertedContracts.drop(3).head
        val CT5 = invertedContracts.drop(4).head
        val CT6 = invertedContracts.drop(5).head

        contractRevisions
            .+:(ContractRevision(uuid = 1, name = "CT1_V1", version = 1, contract = CT1, contractPackageRevisions.head))
            .+:(ContractRevision(uuid = 2, name = "CT1_V2", version = 2, contract = CT1, contractPackageRevisions.head))

            .+:(ContractRevision(uuid = 3, name = "CT2_V1", version = 1, contract = CT2, contractPackageRevisions.drop(1).head))
            .+:(ContractRevision(uuid = 4, name = "CT2_V2", version = 2, contract = CT2, contractPackageRevisions.drop(8).head))

            .+:(ContractRevision(uuid = 5, name = "CT3_V1", version = 1, contract = CT3, contractPackageRevisions.drop(2).head))
            .+:(ContractRevision(uuid = 6, name = "CT3_V2", version = 2, contract = CT3, contractPackageRevisions.drop(6).head))
            .+:(ContractRevision(uuid = 7, name = "CT3_V3", version = 3, contract = CT3, contractPackageRevisions.drop(7).head))

            .+:(ContractRevision(uuid = 8, name = "CT4_V1", version = 1, contract = CT4, contractPackageRevisions.drop(3).head))

            .+:(ContractRevision(uuid = 9, name = "CT5_V2", version = 1, contract = CT5, contractPackageRevisions.drop(4).head))

            .+:(ContractRevision(uuid = 10, name = "CT6_V1", version = 1, contract = CT6, contractPackageRevisions.drop(5).head))
    }

    def randomContractPackages: Seq[ContractPackage] = {
        val contracts = Seq.empty[ContractPackage]

        contracts
            .+:(ContractPackage(uuid = 1, name = "PCK_1"))
            .+:(ContractPackage(uuid = 2, name = "PCK_2"))
            .+:(ContractPackage(uuid = 3, name = "PCK_3"))
            .+:(ContractPackage(uuid = 4, name = "PCK_4"))
            .+:(ContractPackage(uuid = 5, name = "PCK_5"))
            .+:(ContractPackage(uuid = 6, name = "PCK_6"))
            .+:(ContractPackage(uuid = 7, name = "PCK_7"))
            .+:(ContractPackage(uuid = 8, name = "PCK_8"))
           // .+:(ContractPackage(uuid = 9, name = "PCK-9"))
           // .+:(ContractPackage(uuid = 10,name = "PCK10"))
    }

    def randomContractPackageRevisionsFor(contractPackages: Seq[ContractPackage], documentRevisions: Seq[DocumentRevision]): Seq[ContractPackageRevision] = {
        val contractPackageRevisions = Seq.empty[ContractPackageRevision]

        val invertedContractPackages = contractPackages.reverse

        val CPK1 = invertedContractPackages.head
        val CPK2 = invertedContractPackages.drop(1).head
        val CPK3 = invertedContractPackages.drop(2).head
        val CPK4 = invertedContractPackages.drop(3).head
        val CPK5 = invertedContractPackages.drop(4).head
        val CPK6 = invertedContractPackages.drop(5).head

        contractPackageRevisions
            .+:(ContractPackageRevision(uuid = 1, name = "PK1_V1", version = 1, contractPackage = CPK1,
                documents = documentRevisions.filter(revision => Seq(1,3,6,9).contains(revision.uuid))))
            .+:(ContractPackageRevision(uuid = 2, name = "PK1_V2", version = 2, contractPackage = CPK1,
                documents = documentRevisions.filter(revision => Seq(2,4,7,9).contains(revision.uuid))))

            .+:(ContractPackageRevision(uuid = 3, name = "PK2_V1", version = 1, contractPackage = CPK2,
                documents = documentRevisions.filter(revision => Seq(5,8,9,10).contains(revision.uuid))))
            .+:(ContractPackageRevision(uuid = 4, name = "PK2_V2", version = 2, contractPackage = CPK2,
                documents = documentRevisions.filter(revision => Seq(6,8,9,10).contains(revision.uuid))))

            .+:(ContractPackageRevision(uuid = 5, name = "PK3_V1", version = 1, contractPackage = CPK3,
                documents = documentRevisions.filter(revision => Seq(1,3,6,9).contains(revision.uuid))))
            .+:(ContractPackageRevision(uuid = 6, name = "PK3_V2", version = 2, contractPackage = CPK3,
                documents = documentRevisions.filter(revision => Seq(2,3,6,9).contains(revision.uuid))))
            .+:(ContractPackageRevision(uuid = 7, name = "PK3_V3", version = 3, contractPackage = CPK3,
                documents = documentRevisions.filter(revision => Seq(2,4,6,9).contains(revision.uuid))))

            .+:(ContractPackageRevision(uuid = 8, name = "PK4_V1", version = 1, contractPackage = CPK4,
                documents = documentRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))

            .+:(ContractPackageRevision(uuid = 9, name = "PK5_V2", version = 1, contractPackage = CPK5,
                documents = documentRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))

            .+:(ContractPackageRevision(uuid = 10, name = "PK6_V1", version = 1, contractPackage = CPK6,
                documents = documentRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))
    }

    def randomDocuments: Seq[Document] = {
        val documents = Seq.empty[Document]

        documents
            .+:(Document(uuid = 1, name = "DC_1"))
            .+:(Document(uuid = 2, name = "DC_2"))
            .+:(Document(uuid = 3, name = "DC_3"))
            .+:(Document(uuid = 4, name = "DC_4"))
            .+:(Document(uuid = 5, name = "DC_5"))
            .+:(Document(uuid = 6, name = "DC_6"))
            .+:(Document(uuid = 7, name = "DC_7"))
            .+:(Document(uuid = 8, name = "DC_8"))
            //.+:(Document(uuid = 9, name = "DC-9"))
            //.+:(Document(uuid = 10,name = "DC10"))
    }

    /**
      * creates documents revisions with associated revisions linked to sections revisions
      * */
    def randomDocumentRevisionsFor(documents: Seq[Document], sectionRevisions: Seq[SectionRevision]): Seq[DocumentRevision] = {
        val documentRevisions = Seq.empty[DocumentRevision]

        val invertedDocuments = documents.reverse

        val DC1 = invertedDocuments.head
        val DC2 = invertedDocuments.drop(1).head
        val DC3 = invertedDocuments.drop(2).head
        val DC4 = invertedDocuments.drop(3).head
        val DC5 = invertedDocuments.drop(4).head
        val DC6 = invertedDocuments.drop(5).head

        documentRevisions
            .+:(DocumentRevision(uuid = 1, name = "DC1_V1", version = 1, document = DC1,
                sections = sectionRevisions.filter(revision => Seq(1,3,6,9).contains(revision.uuid))))
            .+:(DocumentRevision(uuid = 2, name = "DC1_V2", version = 2, document = DC1,
                sections = sectionRevisions.filter(revision => Seq(2,4,7,9).contains(revision.uuid))))

            .+:(DocumentRevision(uuid = 3, name = "DC2_V1", version = 1, document = DC2,
                sections = sectionRevisions.filter(revision => Seq(5,8,9,10).contains(revision.uuid))))
            .+:(DocumentRevision(uuid = 4, name = "DC2_V2", version = 2, document = DC2,
                sections = sectionRevisions.filter(revision => Seq(6,8,9,10).contains(revision.uuid))))

            .+:(DocumentRevision(uuid = 5, name = "DC3_V1", version = 1, document = DC3,
                sections = sectionRevisions.filter(revision => Seq(1,3,6,9).contains(revision.uuid))))
            .+:(DocumentRevision(uuid = 6, name = "DC3_V2", version = 2, document = DC3,
                sections = sectionRevisions.filter(revision => Seq(2,3,6,9).contains(revision.uuid))))
            .+:(DocumentRevision(uuid = 7, name = "DC3_V3", version = 3, document = DC3,
                sections = sectionRevisions.filter(revision => Seq(2,4,6,9).contains(revision.uuid)))) //Can be synchronised to the latest version

            .+:(DocumentRevision(uuid = 8, name = "DC4_V1", version = 1, document = DC4,
                sections = sectionRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))

            .+:(DocumentRevision(uuid = 9, name = "DC5_V1", version = 1, document = DC5,
                sections = sectionRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))

            .+:(DocumentRevision(uuid = 10, name = "DC6_V1", version = 1, document = DC6,
                sections = sectionRevisions.filter(revision => Seq(1,3,9).contains(revision.uuid))))
    }

    def randomSections: Seq[Section] = {
        val sections = Seq.empty[Section]

        sections
            .+:(Section(uuid = 1, name = "SC_1"))
            .+:(Section(uuid = 2, name = "SC_2"))
            .+:(Section(uuid = 3, name = "SC_3"))
            .+:(Section(uuid = 4, name = "SC_4"))
            .+:(Section(uuid = 5, name = "SC_5"))
            .+:(Section(uuid = 6, name = "SC_6"))
            .+:(Section(uuid = 7, name = "SC_7"))
            .+:(Section(uuid = 8, name = "SC_8"))
            //.+:(Section(uuid = 9, name = "SC-9"))
            //.+:(Section(uuid = 10,name = "SC10"))
    }

    def randomSectionRevisionsFor(sections: Seq[Section]): Seq[SectionRevision] = {
        val sectionRevisions = Seq.empty[SectionRevision]
        //TO make revision namings below aligned
        val invertedSections = sections.reverse

        val SC1 = invertedSections.head
        val SC2 = invertedSections.drop(1).head
        val SC3 = invertedSections.drop(2).head
        val SC4 = invertedSections.drop(3).head
        val SC5 = invertedSections.drop(4).head
        val SC6 = invertedSections.drop(5).head

        sectionRevisions
            .+:(SectionRevision(uuid = 1, name = "SC1_V1", version = 1, section = SC1))
            .+:(SectionRevision(uuid = 2, name = "SC1_V2", version = 2, section = SC1))

            .+:(SectionRevision(uuid = 3, name = "SC2_V1", version = 1, section = SC2))
            .+:(SectionRevision(uuid = 4, name = "SC2_V2", version = 2, section = SC2))

            .+:(SectionRevision(uuid = 5, name = "SC3_V1", version = 1, section = SC3))
            .+:(SectionRevision(uuid = 6, name = "SC3_V2", version = 2, section = SC3))
            .+:(SectionRevision(uuid = 7, name = "SC3_V3", version = 3, section = SC3))

            .+:(SectionRevision(uuid = 8, name = "SC4_V1", version = 1, section = SC4))

            .+:(SectionRevision(uuid = 9, name = "SC5V1", version = 1, section = SC5))

            .+:(SectionRevision(uuid = 10, name = "SC6_V1", version = 1, section = SC6))
    }

}
