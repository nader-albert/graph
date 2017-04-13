package na.services

import na.models.contracts.ContractRevision
import na.models.packages.{ContractPackage, ContractPackageRevision}
import na.repositories.packages.ContractPackageRepository

class PackageService extends TemplateService[ContractPackage] with VersioningService[ContractPackage, ContractPackageRevision] {

  override def add(template: ContractPackage): Unit = ContractPackageRepository.add(template)

  override def getOne(template: ContractPackage): ContractPackage = ???

  //override def update(entity: PackageTemplate): Unit = ???

  //override def delete(entity: Package): Unit = ???

  /**
    * adds a new revision to the given template, and advances the current version to the given one
    **/
  override def addRevision(revision: ContractPackageRevision): Unit = {
      ContractPackageRepository.add(revision)
      ContractPackageRepository.attach(revision.contractPackage, revision)
  }

  def addDocuments(revision: ContractPackageRevision): Unit = {
    revision.documents.foreach(ContractPackageRepository.attach(revision,_))
  }

  /**
    * retrieves the latest revision associated with the given template
    **/
  override def getLatest(template: ContractPackage): ContractPackageRevision = ???

  /**
    * retrieves the original revision associated with the given template
    **/
  override def getOriginal(template: ContractPackage): ContractPackageRevision = ???

  /** *
    * retrieves the next revision after the current one, for the given template
    * */
  override def getNext(template: ContractPackage): ContractPackageRevision = ???

  /** *
    * retrieves the next revision after the given one, for the given template
    * */
  override def getNext(template: ContractPackage, revision: ContractPackageRevision): ContractPackageRevision = ???

  /** *
    * retrieves the previous revision after the current one
    * */
  override def getPrevious(template: ContractPackage): ContractPackageRevision = ???

  /** *
    * retrieves the previous revision after the given one, for the given template
    * */
  override def getPrevious(template: ContractPackage, revision: ContractPackageRevision): ContractPackageRevision = ???

  /** *
    * marks a revision as deleted
    * */
  //override def delete(revision: Package): Package = ???

  /**
    * restores a previously deleted revision
    **/
  override def restore(revision: ContractPackageRevision): ContractPackageRevision = ???

  /** *
    * resets the head of the template to the specified revision
    * */
  override def reset(template: ContractPackage, revision: ContractPackageRevision): ContractPackageRevision = ???

  /** *
    * returns the same version instance with all its composites pushed forward to their latest revisions (HEAD)
    * */
  override def synchronise(revision: ContractPackageRevision): ContractPackageRevision = ???
}
