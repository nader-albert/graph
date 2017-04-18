package na.repositories.documents

import na.models.documents.{Document, DocumentRevision}
import na.models.neo4j.RelTypes
import na.models.sections.SectionRevision
import na.repositories.sections.SectionRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters
import scala.collection.JavaConversions._

object DocumentRepository extends RelationalRepository[DocumentRevision] with GraphRepository[Document, DocumentRevision]{

    /**
      * creates a new template from the specified type
      **/
    override def add(document: Document): Unit = {
        execute {
            (
                CREATE (" %s:%s {uuid: {uuid}, name: {name} }".format(Document.alias, Document.label))
                ,
                parameters(
                    "uuid", document.uuid.toString ,
                    "name", document.name)
            )
        }
    }

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: DocumentRevision): Unit = {
        execute {
            (
                CREATE(" %s:%s {uuid: {uuid}, name: {name} }".format(DocumentRevision.alias, DocumentRevision.label))
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }
    }

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(templateDocument: Document, revision: DocumentRevision): DocumentRevision = {
        execute {
            (
                MATCH(one(templateDocument) and one(revision))
                    andThen
                CREATE(leftToRightLink(Document.alias, RelTypes.HAS_A.name(), DocumentRevision.alias)),

                parameters(
                    "documentName", templateDocument.name,
                    "documentUuid", templateDocument.uuid.toString,
                    "dcRevisionUuid", revision.uuid.toString,
                    "dcRevisionName", revision.name))
        }

        revision
    }

    /***
      * @param documentRevision, the document revision required to be connected to a section revision
      * @param sectionRevision, the section revision required to be connected to a document revision
      * @return a contract revision linked to the given package revision
      * */
    def attach(documentRevision: DocumentRevision, sectionRevision: SectionRevision): Unit = {
        execute {
            (
                MATCH(one(documentRevision) and SectionRepository.one(sectionRevision))
                    andThen
                CREATE(leftToRightLink(DocumentRevision.alias, RelTypes.CONTAINS_A.name(), SectionRevision.alias))
                ,
                parameters(
                    "dcRevisionName", documentRevision.name,
                    "dcRevisionUuid", documentRevision.uuid.toString,
                    "scRevisionName", sectionRevision.name,
                    "scRevisionUuid", sectionRevision.uuid.toString)
            )
        }
    }

    override def attach(currentRevision: DocumentRevision, nextRevision: DocumentRevision): DocumentRevision = {
        execute {
            (
                MATCH(current(currentRevision) and next(nextRevision))
                    andThen
                CREATE(leftToRightLink(DocumentRevision.alias, RelTypes.NEXT.name(), DocumentRevision.alias + "next"))
                    andThen
                CREATE(rightToLeftLink(DocumentRevision.alias, RelTypes.PREVIOUS.name(), DocumentRevision.alias + "next"))
                ,
                parameters(
                    "dcCurrentRevisionName", currentRevision.name,
                    "dcCurrentRevisionUuid", currentRevision.uuid.toString,
                    "dcNextRevisionUuid", nextRevision.uuid.toString,
                    "dcNextRevisionName", nextRevision.name)
            )
        }

        nextRevision
    }

    override def find(document: Document, connectionName: String): Option[DocumentRevision] = {
        execute {
            (
                MATCH(one(document) thatIs connectedTo(connectionName, DocumentRevision.alias))
                    andThen
                    RETURN ("%s, %s, %s".format(Document.alias, "r", DocumentRevision.alias))
                ,
                parameters(
                    "documentName", document.name,
                    "documentUuid", document.uuid.toString
                )
            )
        }.filter {result => result.hasNext}
            .map (result => result.next())
            .map {
                record => DocumentRevision(
                    record.get(DocumentRevision.alias).get("uuid").asString().toLong,
                    record.get(DocumentRevision.alias).get("name").asString(),
                    document)
            }
    }

    override def find(revision: DocumentRevision, connectionName: String): Option[DocumentRevision] = {
        execute {
            (
                MATCH(current(revision) thatIs connectedTo(connectionName, DocumentRevision.alias + "suffix"))
                    andThen
                    RETURN ("%s, %s".format(DocumentRevision.alias + "suffix", "r"))
                ,
                parameters(
                    "dcCurrentRevisionName", revision.name,
                    "dcCurrentRevisionUuid", revision.uuid.toString
                )
            )
        } //TODO: Could be done better by specifying the end of the path in the query itself instead of fetching the last record in the result !
            .filter{result => result.hasNext}
            .map(result => result.list().reverse.last) //TODO: Check if it should really remain reverse or get it back... for some reason I noticed that it doesn't matter ... which of course doesnt make any sense
            .map { record =>
            DocumentRevision(
                record.get(DocumentRevision.alias+"suffix").get("uuid").asString().toLong,
                record.get(DocumentRevision.alias+"suffix").get("name").asString(),
                revision.document)
        }
    }

    def one(document :Document): String =
        "(%s:%s {name:{documentName}, uuid:{documentUuid} } )"
            .format(Document.alias, Document.label)

    def one(documentRevision: DocumentRevision): String =
        "(%s:%s {name:{dcRevisionName}, uuid:{dcRevisionUuid} } )"
            .format(DocumentRevision.alias, DocumentRevision.label)

    def current(documentRevision: DocumentRevision): String =
        "(%s:%s {name:{dcCurrentRevisionName}, uuid:{dcCurrentRevisionUuid} } )"
            .format(DocumentRevision.alias, DocumentRevision.label)

    def next(documentRevision: DocumentRevision): String =
        "(%s:%s {name:{dcNextRevisionName}, uuid:{dcNextRevisionUuid} } )"
            .format(DocumentRevision.alias + "next", DocumentRevision.label) //To differentiate between aliases used in current and next
}
