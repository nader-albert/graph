package na.models.neo4j

import org.neo4j.graphdb.RelationshipType

object RelTypes extends Enumeration {

    val HAS_A = new RelationshipType { override def name(): String = "Has_A"}
    val LINKED_TO = new RelationshipType { override def name(): String = "Linked_To" }
    val CONTAINS_A = new RelationshipType { override def name(): String = "CONTAINS_A"}
}
