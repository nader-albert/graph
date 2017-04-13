package na.repositories.documents

import na.models.documents.{Document, DocumentRevision}
import na.models.packages.ContractPackageRevision
import na.repositories.{GraphRepository, RelationalRepository}

object DocumentRepository extends RelationalRepository[ContractPackageRevision] with GraphRepository[Document, DocumentRevision]{

    /**
      * creates a new template from the specified type
      **/
    override def add(template: Document): Unit = ???

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: DocumentRevision): Unit = ???

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(template: Document, revision: DocumentRevision): DocumentRevision = ???

    override def find(template: Document): DocumentRevision = ???
}
