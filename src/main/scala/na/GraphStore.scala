package na

import org.neo4j.driver.v1.GraphDatabase

/**
  * provides a driver connection to the data store
  * */
object GraphStore {

    /**
      * use AuthTokens.basic("neo4j", "neo4j") as a second parameter if authentication is enabled on the server
      */
    val driver = GraphDatabase.driver("bolt://localhost:7687" /*, AuthTokens.basic("neo4j", "neo4j")*/)

}
