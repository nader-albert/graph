package na.repositories.sections

import na.models.neo4j.RelTypes
import na.models.sections.{Section, SectionRevision}
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

object SectionRepository extends RelationalRepository[SectionRevision] with GraphRepository[Section, SectionRevision]{
    /**
      * creates a new template from the specified type
      **/
    override def add(section: Section): Unit = {
        execute {
            (
                CREATE (" %s:%s {uuid: {uuid}, name: {name} }".format(Section.alias, Section.label))
                ,
                parameters(
                    "uuid", section.uuid.toString ,
                    "name", section.name)
            )
        }
    }

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: SectionRevision): Unit = {
        execute {
            (
                CREATE(" %s:%s {uuid: {uuid}, name: {name} }".format(SectionRevision.alias, SectionRevision.label))
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }
    }

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(section: Section, revision: SectionRevision): SectionRevision = {
        execute {
            (
                MATCH(one(section) and one(revision))
                    andThen
                CREATE(leftLink(Section.alias, RelTypes.HAS_A.name(), SectionRevision.alias)),

                parameters(
                    "sectionName", section.name,
                    "sectionUuid", section.uuid.toString,
                    "scRevisionUuid", revision.uuid.toString,
                    "scRevisionName", revision.name))
        }

        revision
    }



    def one(contract :Section): String =
        "(%s:%s {name:{sectionName}, uuid:{sectionUuid} } )"
            .format(Section.alias, Section.label)

    def one(contractRevision: SectionRevision): String =
        "(%s:%s {name:{scRevisionName}, uuid:{scRevisionUuid} } )"
            .format(SectionRevision.alias, SectionRevision.label)

    override def attach(previousRevision: SectionRevision, nextRevision: SectionRevision): SectionRevision = ???

    override def find(template: Section, connectionName: String): Option[SectionRevision] = ???

    override def find(currentRevision: SectionRevision, connectionName: String): Option[SectionRevision] = ???
}
