package na.services

import na.models.{Entity, Template, Versioned}

trait VersioningService[A <: Template, B <: Entity with Versioned] {

  /**
    * adds a new revision to the given template
    * */
    def addRevision(template: A, revision: B)

    /**
      * retrieves the latest revision associated with the given template
      * */
    def getLatest(template: A): B

    /**
      * retrieves the original revision associated with the given template
      * */
    def getOriginal(template: A): B

    /***
      * retrieves the next revision after the current one
      * */
    def getNext(revision: B): B

    /***
      * retrieves the previous revision after the current one
      * */
    def getPrevious(revision: B): B

    /***
      * marks a revision as deleted
      * */
    def delete(revision: B): B

    /**
      * restores a previously deleted revision
      * */
    def restore(revision: B): B

    /***
      * resets the head of the template to the specified revision
      * */
    def reset(template: A, revision:B): B

    /***
      * returns the same version instance with all its composites pushed forward to their latest revisions (HEAD)
      * */
    def synchronise(revision: B): B
}
