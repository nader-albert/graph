package na.repositories

import na.GraphStore
import na.models.{Entity, Template, Versioned}
import org.neo4j.driver.v1.{StatementResult, Value}

trait GraphRepository[A <: Template, B <: Entity with Versioned] {

    //provides basic information concerning the underlying graph data structure and the required access points to it (encloses a graph driver, maybe ?!)
    //provides core graph database access APIs

    /**
      * creates a new template from the specified type
      * */
    def add(template: A): Unit

    /**
      * creates a new template from the specified type
      * */
    def add(revision: B): Unit

    /**
      * creates a new version and link it with the given template
      * */
    def attach(template: A, revision: B): B

    def attach(previousRevision: B, nextRevision: B): B

    def find(template: A, connectionName: String): Option[B]

    def find(currentRevision: B, connectionName: String): Option[B]

    def execute(statement: => (String, Value)): Option[StatementResult] = {
        val session = GraphStore.driver.session

        val tx = session.beginTransaction

        try {
            val result = tx.run(statement._1, statement._2)
            tx.success()
            Some(result)
        } catch {
            case ex: Exception =>
                tx.failure()
                None
        } finally if (tx != null) tx.close()
    }

    protected def MATCH(statement: => String): String = "MATCH" + statement

    protected def RETURN(statement: => String): String = "RETURN " + statement

    protected def CREATE(statement: => String): String = "CREATE (%s)".format(statement)

    /**
      * connects the left hand to the right hand side
      * creates a relationship that is directed from left to right
      * */
    def leftLink(left: String, to: String, right: String): String =
        "(%s)-[rLeft:%s]->(%s)"
            .format(left, to, right)

    /**
      * connects the right hand to the left hand side
      * creates a relationship that is directed from right to left
      * */
    def rightLink(left: String, to: String, right: String): String =
        "(%s)-[rRight:%s]->(%s)"
            .format(right, to, left)

    def connectedTo(connection: String, target: String): String =
        "-[r:%s]->(%s)".format(connection, target)

    implicit class StringExt(leftSide: String) {
        def and(rightSide: String): String = leftSide + ", " + rightSide

        def thatIs(rightSide: String): String = leftSide + " " + rightSide

        def andThen(rightSide: String): String = leftSide + " " + rightSide
    }
}
