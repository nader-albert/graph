package na.repositories.sections

import na.models.neo4j.RelTypes
import na.models.sections.{Section, SectionRevision}
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters
import scala.collection.JavaConversions._

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
                CREATE(leftToRightLink(Section.alias, RelTypes.HAS_A.name(), SectionRevision.alias)),

                parameters(
                    "sectionName", section.name,
                    "sectionUuid", section.uuid.toString,
                    "scRevisionUuid", revision.uuid.toString,
                    "scRevisionName", revision.name))
        }

        revision
    }

    override def attach(currentRevision: SectionRevision, nextRevision: SectionRevision): SectionRevision = {
        execute {
            (
                MATCH(current(currentRevision) and next(nextRevision))
                    andThen
                CREATE(leftToRightLink(SectionRevision.alias, RelTypes.NEXT.name(), SectionRevision.alias + "next"))
                    andThen
                CREATE(rightToLeftLink(SectionRevision.alias, RelTypes.PREVIOUS.name(), SectionRevision.alias + "next"))
                ,
                parameters(
                    "scCurrentRevisionName", currentRevision.name,
                    "scCurrentRevisionUuid", currentRevision.uuid.toString,
                    "scNextRevisionUuid", nextRevision.uuid.toString,
                    "scNextRevisionName", nextRevision.name)
            )
        }

        nextRevision
    }

    override def find(section: Section, connectionName: String): Option[SectionRevision] = {
        execute {
            (
                MATCH(one(section) thatIs connectedTo(connectionName, SectionRevision.alias))
                    andThen
                    RETURN ("%s, %s, %s".format(Section.alias, "r", SectionRevision.alias))
                ,
                parameters(
                    "sectionName", section.name,
                    "sectionUuid", section.uuid.toString
                )
            )
        }.filter {result => result.hasNext}
            .map (result => result.next())
            .map {
                record => SectionRevision(
                    record.get(SectionRevision.alias).get("uuid").asString().toLong,
                    record.get(SectionRevision.alias).get("name").asString(),
                    section)
            }
    }

    override def find(revision: SectionRevision, connectionName: String): Option[SectionRevision] = {
        execute {
            (
                MATCH(current(revision) thatIs connectedTo(connectionName, SectionRevision.alias + "suffix"))
                    andThen
                RETURN ("%s, %s".format(SectionRevision.alias + "suffix", "r"))
                ,
                parameters(
                    "scCurrentRevisionName", revision.name,
                    "scCurrentRevisionUuid", revision.uuid.toString
                )
            )
        } //TODO: Could be done better by specifying the end of the path in the query itself instead of fetching the last record in the result !
            .filter{result => result.hasNext}
            .map(result => result.list().reverse.last) //TODO: Check if it should really remain reverse or get it back... for some reason I noticed that it doesn't matter ... which of course doesnt make any sense
            .map { record =>
                SectionRevision(
                    record.get(SectionRevision.alias+"suffix").get("uuid").asString().toLong,
                    record.get(SectionRevision.alias+"suffix").get("name").asString(),
                    revision.section)
        }
    }

    def one(contract :Section): String =
        "(%s:%s {name:{sectionName}, uuid:{sectionUuid} } )"
            .format(Section.alias, Section.label)

    def one(contractRevision: SectionRevision): String =
        "(%s:%s {name:{scRevisionName}, uuid:{scRevisionUuid} } )"
            .format(SectionRevision.alias, SectionRevision.label)

    def current(sectionRevision: SectionRevision): String =
        "(%s:%s {name:{scCurrentRevisionName}, uuid:{scCurrentRevisionUuid} } )"
            .format(SectionRevision.alias, SectionRevision.label)

    def next(sectionRevision: SectionRevision): String =
        "(%s:%s {name:{scNextRevisionName}, uuid:{scNextRevisionUuid} } )"
            .format(SectionRevision.alias + "next", SectionRevision.label) //To differentiate between aliases used in current and next
}
