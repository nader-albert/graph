package na

import na.models.neo4j.RelTypes
import org.neo4j.graphdb.{GraphDatabaseService, Label, Node, Relationship}
import Label.label
import na.models.Document
import na.models.contracts.{Contract, ContractRevision}

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
            .+:(Contract(id=1, name = "CT1"))
            .+:(Contract(id=2, name = "CT2"))
            .+:(Contract(id=3, name = "CT3"))
            .+:(Contract(id=4, name = "CT4"))
            .+:(Contract(id=5, name = "CT5"))
            .+:(Contract(id=6, name = "CT6"))
            .+:(Contract(id=7, name = "CT7"))
            .+:(Contract(id=8, name = "CT8"))
            .+:(Contract(id=9, name = "CT9"))
            .+:(Contract(id=10,name = "CT10"))
    }

    def randomContractRevisionsFor(contracts: Seq[Contract]): Seq[ContractRevision] = {
        val contractRevisions = Seq.empty[ContractRevision]

        contractRevisions
            .+:(ContractRevision(id=1, name = "CTV1", version = 1, contracts.head, null))
            .+:(ContractRevision(id=2, name = "CTV2", version = 2, contracts.head, null))

            .+:(ContractRevision(id=3, name = "CTV1", version = 1, contracts.drop(1).head, null))
            .+:(ContractRevision(id=4, name = "CTV2", version = 2, contracts.drop(1).head, null))

            .+:(ContractRevision(id=5, name = "CTV1", version = 1, contracts.drop(2).head, null))
            .+:(ContractRevision(id=6, name = "CTV2", version = 2, contracts.drop(2).head, null))
            .+:(ContractRevision(id=7, name = "CTV3", version = 3, contracts.drop(2).head, null))

            .+:(ContractRevision(id=8, name = "CTV1", version = 1, contracts.drop(3).head, null))

            .+:(ContractRevision(id=9, name = "CTV2", version = 1, contracts.drop(4).head, null))

            .+:(ContractRevision(id=10, name = "CTV1", version = 1, contracts.drop(5).head, null))
    }
}
