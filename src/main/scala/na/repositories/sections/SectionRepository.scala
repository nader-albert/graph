package na.repositories.sections

import na.models.sections.{Section, SectionRevision}
import na.repositories.{GraphRepository, RelationalRepository}

object SectionRepository extends RelationalRepository[SectionRevision] with GraphRepository[Section, SectionRevision]{
    /**
      * creates a new template from the specified type
      **/
    override def add(template: Section): Unit = ???

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: SectionRevision): Unit = ???

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(template: Section, revision: SectionRevision): SectionRevision = ???

    override def find(template: Section): SectionRevision = ???
}
