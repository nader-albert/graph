package na.repositories

import na.GraphStore
import na.models.{Entity, Template, Versioned}
import org.neo4j.driver.v1.{StatementResult, Value}

trait GraphRepository[A <: Template, B <: Entity with Versioned] {

    protected val templateAlias: String

    protected val reversionAlias = "revision"

    //provides basic information concerning the underlying graph data structure and the required access points to it (encloses a graph driver, maybe ?!)
    //provides core graph database access APIs

    /**
      * creates a new template from the specified type
      * */
    def add(template: A): A

    /**
      * creates a new template from the specified type
      * */
    def add(revision: B): B

    /**
      * creates a new version and link it with the given template
      * */
    def attach(template: A, revision: B): B

    def find(template: A): B

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

    protected def CREATE(statement: => String): String = "CREATE" + statement

    implicit class StringExt(leftSide: String) {
        def and (rightSide: String): String = leftSide + ", " + rightSide

        def andThen(rightSide: String): String = leftSide + " " + rightSide
    }
}
