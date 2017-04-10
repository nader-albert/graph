package na

import na.models.neo4j.RelTypes
import org.neo4j.graphdb.{GraphDatabaseService, Label, Node, Relationship}
import Label.label

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
