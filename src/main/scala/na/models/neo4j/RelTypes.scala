package na.models.neo4j

import org.neo4j.graphdb.RelationshipType

object RelTypes extends Enumeration {

    val HAS_A = new RelationshipType { override def name(): String = "HAS_A"}
    val LINKED_TO = new RelationshipType { override def name(): String = "LINKED_To" }
    val CONTAINS_A = new RelationshipType { override def name(): String = "CONTAINS_A"}
}
