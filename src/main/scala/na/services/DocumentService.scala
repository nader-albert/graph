package na.services

import na.models.documents.{Document, DocumentRevision}

class DocumentService extends TemplateService[Document] with VersioningService[Document, DocumentRevision] {

    override def add(template: Document): Unit = ???

    override def getOne(template: Document): Document = ???

    /**
      * adds a new revision to the given template, and advances the current version to the given one
      **/
    override def addRevision(revision: DocumentRevision): Unit = ???

    /**
      * retrieves the latest revision associated with the given template
      **/
    override def getLatest(template: Document): DocumentRevision = ???

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

}
