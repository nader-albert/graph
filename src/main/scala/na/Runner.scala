package na

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.repositories.contract.ContractRepository
import na.services.ContractService

import ContractPool._

object Runner extends App {

    val contractService = new ContractService

    remote
    //runEmbedded

    private def remote = {
        println("remote is here !")

        ContractPool.randomContracts.foreach{contractService.add}

        randomContractRevisionsFor(randomContracts).foreach(revision => contractService.addRevision(revision))
    }

    private def embedded = {
        val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/Users/nader/neo4j/data/databases/contracts.db"))
        //registerShutdownHook(graphDb)

        println("embedded is here !")

        val pool = new ContractPool(graphDb)

        graphDb.shutdown
    }
}

