package na

import na.models.neo4j.RelTypes
import org.neo4j.graphdb.{GraphDatabaseService, Label, Node, Relationship}
import Label.label
import na.models.contracts.{Contract, ContractRevision}
import na.models.packages.{ContractPackage, ContractPackageRevision}

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
            .+:(Contract(uuid = 1, name = "CT1"))
            .+:(Contract(uuid = 2, name = "CT2"))
            .+:(Contract(uuid = 3, name = "CT3"))
            .+:(Contract(uuid = 4, name = "CT4"))
            .+:(Contract(uuid = 5, name = "CT5"))
            .+:(Contract(uuid = 6, name = "CT6"))
            .+:(Contract(uuid = 7, name = "CT7"))
            .+:(Contract(uuid = 8, name = "CT8"))
            .+:(Contract(uuid = 9, name = "CT9"))
            .+:(Contract(uuid = 10,name = "CT10"))
    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    def randomContractRevisionsFor(contracts: Seq[Contract], contractPackageRevisions: Seq[ContractPackageRevision]): Seq[ContractRevision] = {
        val contractRevisions = Seq.empty[ContractRevision]

        contractRevisions
            .+:(ContractRevision(uuid = 1, name = "CTV1", version = 1, contracts.head, contractPackageRevisions.head))
            .+:(ContractRevision(uuid = 2, name = "CTV2", version = 2, contracts.head, contractPackageRevisions.head))

            .+:(ContractRevision(uuid = 3, name = "CTV1", version = 1, contracts.drop(1).head, contractPackageRevisions.drop(1).head))
            .+:(ContractRevision(uuid = 4, name = "CTV2", version = 2, contracts.drop(1).head, contractPackageRevisions.drop(8).head))

            .+:(ContractRevision(uuid = 5, name = "CTV1", version = 1, contracts.drop(2).head, contractPackageRevisions.drop(2).head))
            .+:(ContractRevision(uuid = 6, name = "CTV2", version = 2, contracts.drop(2).head, contractPackageRevisions.drop(6).head))
            .+:(ContractRevision(uuid = 7, name = "CTV3", version = 3, contracts.drop(2).head, contractPackageRevisions.drop(7).head))

            .+:(ContractRevision(uuid = 8, name = "CTV1", version = 1, contracts.drop(3).head, contractPackageRevisions.drop(3).head))

            .+:(ContractRevision(uuid = 9, name = "CTV2", version = 1, contracts.drop(4).head, contractPackageRevisions.drop(4).head))

            .+:(ContractRevision(uuid = 10, name = "CTV1", version = 1, contracts.drop(5).head, contractPackageRevisions.drop(5).head))
    }

    def randomContractPackages: Seq[ContractPackage] = {
        val contracts = Seq.empty[ContractPackage]

        contracts
            .+:(ContractPackage(uuid = 1, name = "PCK1"))
            .+:(ContractPackage(uuid = 2, name = "PCK2"))
            .+:(ContractPackage(uuid = 3, name = "PCK3"))
            .+:(ContractPackage(uuid = 4, name = "PCK4"))
            .+:(ContractPackage(uuid = 5, name = "PCK5"))
            .+:(ContractPackage(uuid = 6, name = "PCK6"))
            .+:(ContractPackage(uuid = 7, name = "PCK7"))
            .+:(ContractPackage(uuid = 8, name = "PCK8"))
            .+:(ContractPackage(uuid = 9, name = "PCK9"))
            .+:(ContractPackage(uuid = 10,name = "PCK0"))
    }

    def randomContractPackageRevisionsFor(contractPackages: Seq[ContractPackage]): Seq[ContractPackageRevision] = {
        val contractPackageRevisions = Seq.empty[ContractPackageRevision]

        contractPackageRevisions
            .+:(ContractPackageRevision(uuid = 1, name = "PCKV1", version = 1, contractPackages.head))
            .+:(ContractPackageRevision(uuid = 2, name = "PCKV2", version = 2, contractPackages.head))

            .+:(ContractPackageRevision(uuid = 3, name = "PCKV1", version = 1, contractPackages.drop(1).head))
            .+:(ContractPackageRevision(uuid = 4, name = "PCKV2", version = 2, contractPackages.drop(1).head))

            .+:(ContractPackageRevision(uuid = 5, name = "PCKV1", version = 1, contractPackages.drop(2).head))
            .+:(ContractPackageRevision(uuid = 6, name = "PCKV2", version = 2, contractPackages.drop(2).head))
            .+:(ContractPackageRevision(uuid = 7, name = "PCKV3", version = 3, contractPackages.drop(2).head))

            .+:(ContractPackageRevision(uuid = 8, name = "PCKV1", version = 1, contractPackages.drop(3).head))

            .+:(ContractPackageRevision(uuid = 9, name = "PCKV2", version = 1, contractPackages.drop(4).head))

            .+:(ContractPackageRevision(uuid = 10, name = "PCKV1", version = 1, contractPackages.drop(5).head))
    }

}
