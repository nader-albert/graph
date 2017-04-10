package na.services

import na.models.Package
import na.models.Package

class PackageService extends TemplateService[Package] with VersioningService[Package, Package] {

  override def add(template: Package): Unit = ???

  override def getOne(template: Package): Package = ???

  //override def update(entity: PackageTemplate): Unit = ???

  override def delete(entity: Package): Unit = ???

  /**
    * adds a new revision to the given template, and advances the current version to the given one
    **/
  override def addRevision(template: Package, revision: Package): Unit = ???

  /**
    * retrieves the latest revision associated with the given template
    **/
  override def getLatest(template: Package): Package = ???

  /**
    * retrieves the original revision associated with the given template
    **/
  override def getOriginal(template: Package): Package = ???

  /** *
    * retrieves the next revision after the current one, for the given template
    * */
  override def getNext(template: Package): Package = ???

  /** *
    * retrieves the next revision after the given one, for the given template
    * */
  override def getNext(template: Package, revision: Package): Package = ???

  /** *
    * retrieves the previous revision after the current one
    * */
  override def getPrevious(template: Package): Package = ???

  /** *
    * retrieves the previous revision after the given one, for the given template
    * */
  override def getPrevious(template: Package, revision: Package): Package = ???

  /** *
    * marks a revision as deleted
    * */
  override def delete(revision: Package): Package = ???

  /**
    * restores a previously deleted revision
    **/
  override def restore(revision: Package): Package = ???

  /** *
    * resets the head of the template to the specified revision
    * */
  override def reset(template: Package, revision: Package): Package = ???

  /** *
    * returns the same version instance with all its composites pushed forward to their latest revisions (HEAD)
    * */
  override def synchronise(revision: Package): Package = ???
}