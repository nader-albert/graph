package na.apps

import java.io.File

import org.neo4j.graphdb.factory.GraphDatabaseFactory
import na.services.{ContractService, DocumentService, PackageService, SectionService}
import na.ContractPool._
import na.ContractPool
import na.models.contracts.ContractRevision
import na.models.documents.DocumentRevision
import na.models.packages.ContractPackageRevision
import na.models.sections.SectionRevision

object Constructor extends App {

    val sectionService = new SectionService
    val packageService = new PackageService
    val contractService = new ContractService
    val documentService = new DocumentService

    val dbPath = "/Users/nader/neo4j/data/databases/contracts.db"

    remote
    //runEmbedded

    private def remote = {
        println("step1: creating sections......")
        val sectionRevisions = createSections()

        println("step2: creating documents.........")
        val documentsRevisions = createDocuments(sectionRevisions)

        println("step3: creating packages............")
        val contractPackageRevisions = createPackages(documentsRevisions)

        println("step4: creating contracts...............")
        val contractRevisions = createContracts(contractPackageRevisions)

        /*println("step5: linking contracts to packages..................")
        linkContractToPackages(contractRevisions)

        println("step6: linking packages to documents .....................")
        linkPackagesToDocuments(contractPackageRevisions)

        println("step7: linking documents to sections ...........................")
        linkDocumentsToSections(documentsRevisions)*/

    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    private def createSections(): Seq[SectionRevision] = {
        val sections = randomSections

        sections.foreach{sectionService.add}

        val revisions = randomSectionRevisionsFor(sections)

        revisions.foreach(revision => sectionService.addRevision(revision))

        revisions
    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    private def createDocuments(sectionRevisions: Seq[SectionRevision]): Seq[DocumentRevision] = {
        val documents = randomDocuments

        documents.foreach{documentService.add}

        val revisions = randomDocumentRevisionsFor(documents, sectionRevisions)

        revisions.foreach(revision => documentService.addRevision(revision))

        revisions
    }

    /**
      * creates packages with associated revisions
      * */
    private def createPackages(documentRevisions: Seq[DocumentRevision]): Seq[ContractPackageRevision] = {
        val contractPackages = randomContractPackages

        contractPackages.foreach(packageService.add)

        val revisions = randomContractPackageRevisionsFor(contractPackages, documentRevisions)

        revisions.foreach(revision => packageService.addRevision(revision))

        revisions
    }

    /**
      * creates contracts with associated revisions linked to package revisions
      * */
    private def createContracts(contractPackageRevisions: Seq[ContractPackageRevision]): Seq[ContractRevision] = {
        val contracts = randomContracts

        contracts.foreach{contractService.add}

        val revisions = randomContractRevisionsFor(contracts, contractPackageRevisions)

        revisions.foreach(revision => contractService.addRevision(revision))

        revisions
    }

    private def linkContractToPackages(contractRevisions: Seq[ContractRevision]) = {
        contractRevisions.foreach(contractService.addPackage)
    }

    private def linkPackagesToDocuments(packageRevisions: Seq[ContractPackageRevision]) = {
        packageRevisions.foreach(packageService.addDocuments)
    }

    private def linkDocumentsToSections(documentsRevisions: Seq[DocumentRevision]) = {
        documentsRevisions.foreach(documentService.addSections)
    }

    private def embedded() = {
        val graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath))
        //registerShutdownHook(graphDb)

        println("embedded is here !")

        val pool = new ContractPool(graphDb)

        graphDb.shutdown
    }


}

