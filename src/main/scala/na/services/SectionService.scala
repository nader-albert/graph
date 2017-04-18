package na.services

import na.models.neo4j.RelTypes
import na.models.sections.{Section, SectionRevision}
import na.repositories.sections.SectionRepository

class SectionService  extends TemplateService[Section] with VersioningService[Section, SectionRevision] {

    override def add(template: Section): Unit = SectionRepository.add(template)

    override def getOne(template: Section): Section = ???

    /**
      * adds a new revision to the given template, and advances the current version to the given one
      **/
    override def addRevision(revision: SectionRevision): Unit = {
        SectionRepository.add(revision)

        // Try to get the latest version attached with enclosed contract, if found attach the current version to it as the next version,
        // if not attach the current version to parent contract

        val currentVersion = getLatest(revision.section)

        if(currentVersion.isDefined) {
            SectionRepository.attach(currentVersion.get, revision)
        }
        else {
            SectionRepository.attach(revision.section, revision)
        }
    }

    /**
      * retrieves the latest revision associated with the given template
      **/
    override def getLatest(template: Section): Option[SectionRevision] = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        //findInGDM(findInRDM(id))

        // Get the first Revision attached to this contract -if any-
        val firstRevision = SectionRepository.find(template, RelTypes.HAS_A.name())

        if(firstRevision.isDefined) {
            val lastRevision = SectionRepository.find(firstRevision.get, RelTypes.NEXT.name())

            if(lastRevision.isDefined) lastRevision else firstRevision
        }
        else
            None
    }

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
