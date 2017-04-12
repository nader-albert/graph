package na

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.models.contracts.Contract
import na.repositories.contract.ContractRepository
import na.services.ContractService

import ContractPool._

object Runner extends App {

    val service = new ContractService(Contract(id =1, name = "Template1"))

    //print(pool)

    //print(pool.count)

    //new Driver

    remote

    //runEmbedded

    private def remote = {
        println("remote is here !")

        ContractPool.randomContracts.foreach{ContractRepository.create}

        randomContractRevisionsFor(randomContracts).foreach(revision => ContractRepository.create(revision.contract, revision))
    }

    private def embedded = {
        val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/Users/nader/neo4j/data/databases/contracts.db"))
        //registerShutdownHook(graphDb)

        println("embedded is here !")

        val pool = new ContractPool(graphDb)

        graphDb.shutdown
    }
}

