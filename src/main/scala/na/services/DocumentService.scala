package na.services

import na.models.documents.{Document, DocumentRevision}
import na.models.neo4j.RelTypes
import na.repositories.documents.DocumentRepository

class DocumentService extends TemplateService[Document] with VersioningService[Document, DocumentRevision] {

    override def add(template: Document): Unit = DocumentRepository.add(template)

    override def getOne(template: Document): Document = ???

    /**
      * adds a new revision to the given template, and advances the current version to the given one
      **/
    override def addRevision(revision: DocumentRevision): Unit = {
        DocumentRepository.add(revision)

        // Try to get the latest version attached with enclosed contract, if found attach the current version to it as the next version,
        // if not attach the current version to parent contract

        val currentVersion = getLatest(revision.document)

        if(currentVersion.isDefined) {
            DocumentRepository.attach(currentVersion.get, revision)
        }
        else {
            DocumentRepository.attach(revision.document, revision)
        }
    }

    def addSections(revision: DocumentRevision): Unit = {
        revision.sections.foreach(DocumentRepository.attach(revision,_))
    }

    /**
      * retrieves the latest revision associated with the given template
      **/
    override def getLatest(template: Document): Option[DocumentRevision] = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        //findInGDM(findInRDM(id))

        // Get the first Revision attached to this contract -if any-
        val firstRevision = DocumentRepository.find(template, RelTypes.HAS_A.name())

        if(firstRevision.isDefined) {
            val lastRevision = DocumentRepository.find(firstRevision.get, RelTypes.NEXT.name())

            if(lastRevision.isDefined) lastRevision else firstRevision
        }
        else
            None
    }

    /**
      * retrieves the original revision associated with the given template
      **/
    override def getOriginal(template: Document): DocumentRevision = ???

    /** *
      * retrieves the next revision after the current one, for the given template
      * */
    override def getNext(template: Document): DocumentRevision = ???

    /** *
      * retrieves the next revision after the given one, for the given template
      * */
    override def getNext(template: Document, revision: DocumentRevision): DocumentRevision = ???

    /** *
      * retrieves the previous revision after the current one
      * */
    override def getPrevious(template: Document): DocumentRevision = ???

    /** *
      * retrieves the previous revision after the given one, for the given template
      * */
    override def getPrevious(template: Document, revision: DocumentRevision): DocumentRevision = ???

    /**
      * restores a previously deleted revision
      **/
    override def restore(revision: DocumentRevision): DocumentRevision = ???

    /** *
      * resets the head of the template to the specified revision
      * */
    override def reset(template: Document, revision: DocumentRevision): DocumentRevision = ???

    /** *
      * returns the same version instance with all its composites pushed forward to their latest revisions (HEAD)
      * */
    override def synchronise(revision: DocumentRevision): DocumentRevision = ???

}
