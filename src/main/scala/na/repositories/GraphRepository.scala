package na.repositories

import na.models.{Entity, Template, Versioned}
import org.neo4j.driver.v1.{GraphDatabase, StatementResult, Value}
import org.neo4j.driver.v1.Values.parameters
import org.neo4j.graphdb.Node

trait GraphRepository[A <: Template, B <: Entity with Versioned] {

    //provides basic information concerning the underlying graph data structure and the required access points to it (encloses a graph driver, maybe ?!)
    //provides core graph database access APIs

    /**
      * use AuthTokens.basic("neo4j", "neo4j") as a second parameter if authentication is enabled on the server
      */
    private val driver = GraphDatabase.driver("bolt://localhost:7687" /*, AuthTokens.basic("neo4j", "neo4j")*/)

    /**
      * creates a new template from the specified type
      * */
    def add(template: A): A = ???

    /**
      * creates a new template from the specified type
      * */
    def add(instance: B): B = ???

    /**
      * creates a new version and link it with the given template
      * */
    def attach(template: A, entity: B): B

    def find(template: A): B = ???

    protected def execute(statement: => (String, Value)): Option[StatementResult] = {
        val session = driver.session

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
}
