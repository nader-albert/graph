package na.repositories.documents

import na.models.documents.{Document, DocumentRevision}
import na.models.neo4j.RelTypes
import na.models.sections.SectionRevision
import na.repositories.sections.SectionRepository
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

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
                CREATE(leftLink(Document.alias, RelTypes.HAS_A.name(), DocumentRevision.alias)),

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
                CREATE(leftLink(DocumentRevision.alias, RelTypes.CONTAINS_A.name(), SectionRevision.alias))
                ,
                parameters(
                    "dcRevisionName", documentRevision.name,
                    "dcRevisionUuid", documentRevision.uuid.toString,
                    "scRevisionName", sectionRevision.name,
                    "scRevisionUuid", sectionRevision.uuid.toString)
            )
        }
    }

    def one(contract :Document): String =
        "(%s:%s {name:{documentName}, uuid:{documentUuid} } )"
            .format(Document.alias, Document.label)

    def one(contractRevision: DocumentRevision): String =
        "(%s:%s {name:{dcRevisionName}, uuid:{dcRevisionUuid} } )"
            .format(DocumentRevision.alias, DocumentRevision.label)

    override def attach(previousRevision: DocumentRevision, nextRevision: DocumentRevision): DocumentRevision = ???

    override def find(template: Document, connectionName: String): Option[DocumentRevision] = ???

    override def find(currentRevision: DocumentRevision, connectionName: String): Option[DocumentRevision] = ???
}
