package na

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.models.Contract
import na.models.neo4j.Driver
import na.services.ContractService

object Runner extends App {

    val service = new ContractService(Contract(id =1, name = "Template1"))

    //val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/Users/nader/neo4j/data/databases/contracts.db"))
    //registerShutdownHook(graphDb)

    println("nader is here !")

    //val pool = new ContractPool(graphDb)

    //print(pool)

    //print(pool.count)

    new Driver

    //graphDb.shutdown
}
