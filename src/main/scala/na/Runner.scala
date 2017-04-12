package na

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.services.{ContractService, PackageService}
import ContractPool._
import na.models.contracts.ContractRevision
import na.models.packages.ContractPackageRevision

object Runner extends App {

    val contractService = new ContractService
    val packageService = new PackageService

    val dbPath = "/Users/nader/neo4j/data/databases/contracts.db"

    remote
    //runEmbedded

    private def remote = {
        println("creating contract packages......")
        val contractPackageRevisions = createContractPackages()

        println("creating contracts......")
        val contractRevisions = createContracts(contractPackageRevisions)

        println("linking contracts to packages......")
        linkContractToPackages(contractRevisions)

        //println("creating package documents .........")

        //println("linking packages to documents .........")

    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    private def createContracts(contractPackageRevisions: Seq[ContractPackageRevision]): Seq[ContractRevision] = {
        val contracts = ContractPool.randomContracts

        contracts.foreach{contractService.add}

        val revisions = randomContractRevisionsFor(contracts, contractPackageRevisions)

        revisions.foreach(revision => contractService.addRevision(revision))

        revisions
    }

    /**
      * creates packages with associated revisions
      * */
    private def createContractPackages(): Seq[ContractPackageRevision] = {
        val contractPackages = ContractPool.randomContractPackages

        contractPackages.foreach(packageService.add)

        val revisions = randomContractPackageRevisionsFor(contractPackages)

        revisions.foreach(revision => packageService.addRevision(revision))

        revisions
    }

    private def linkContractToPackages(contractRevisions: Seq[ContractRevision]) = {
        contractRevisions.foreach(contractService.addPackage)
    }

    private def embedded() = {
        val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath))
        //registerShutdownHook(graphDb)

        println("embedded is here !")

        val pool = new ContractPool(graphDb)

        graphDb.shutdown
    }


}

