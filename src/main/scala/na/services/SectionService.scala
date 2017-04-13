package na.services

import na.models.documents.DocumentRevision
import na.models.packages.ContractPackageRevision
import na.models.sections.{Section, SectionRevision}
import na.repositories.packages.ContractPackageRepository
import na.repositories.sections.SectionRepository

class SectionService  extends TemplateService[Section] with VersioningService[Section, SectionRevision] {

    override def add(template: Section): Unit = SectionRepository.add(template)

    override def getOne(template: Section): Section = ???

    /**
      * adds a new revision to the given template, and advances the current version to the given one
      **/
    override def addRevision(revision: SectionRevision): Unit = {
        SectionRepository.add(revision)
        SectionRepository.attach(revision.section, revision)
    }

    /**
      * retrieves the latest revision associated with the given template
      **/
    override def getLatest(template: Section): Option[SectionRevision] = ???

    /**
      * retrieves the original revision associated with the given template
      **/
    override def getOriginal(template: Section): SectionRevision = ???

    /** *
      * retrieves the next revision after the current one, for the given template
      * */
    override def getNext(template: Section): SectionRevision = ???

    /** *
      * retrieves the next revision after the given one, for the given template
      * */
    override def getNext(template: Section, revision: SectionRevision): SectionRevision = ???

    /** *
      * retrieves the previous revision after the current one
      * */
    override def getPrevious(template: Section): SectionRevision = ???

    /** *
      * retrieves the previous revision after the given one, for the given template
      * */
    override def getPrevious(template: Section, revision: SectionRevision): SectionRevision = ???

    /**
      * restores a previously deleted revision
      **/
    override def restore(revision: SectionRevision): SectionRevision = ???

    /** *
      * resets the head of the template to the specified revision
      * */
    override def reset(template: Section, revision: SectionRevision): SectionRevision = ???

    /** *
      * returns the same version instance with all its composites pushed forward to their latest revisions (HEAD)
      * */
    override def synchronise(revision: SectionRevision): SectionRevision = ???
}
