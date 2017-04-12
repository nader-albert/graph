package na

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.services.{ContractService, PackageService}
import ContractPool._

object Runner extends App {

    val contractService = new ContractService
    val packageService = new PackageService

    val dbPath = "/Users/nader/neo4j/data/databases/contracts.db"

    remote
    //runEmbedded

    private def remote = {
        println("creating contracts......")
        createContracts()

        println("creating contract packages......")
        createContractPackages()
    }

    private def embedded() = {
        val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath))
        //registerShutdownHook(graphDb)

        println("embedded is here !")

        val pool = new ContractPool(graphDb)

        graphDb.shutdown
    }

    private def createContracts() = {
        val contracts = ContractPool.randomContracts

        contracts.foreach{contractService.add}

        randomContractRevisionsFor(contracts).foreach(revision => contractService.addRevision(revision))
    }

    private def createContractPackages() = {
        val contractPackages = ContractPool.randomContractPackages

        contractPackages.foreach{packageService.add}

        randomContractPackageRevisionsFor(contractPackages).foreach(revision => packageService.addRevision(revision))
    }

}

