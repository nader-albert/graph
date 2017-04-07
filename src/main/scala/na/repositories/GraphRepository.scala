package na.repositories

import org.neo4j.graphdb.Node

trait GraphRepository[T] {

    //provides basic information concerning the underlying graph data structure and the required access points to it (encloses a graph driver, maybe ?!)
    //provides core graph database access APIs

    /**
      * Finds an entity corresponding to the one passed in the parameter. basically
      * @param entity, the entity, whose corresponding node is required to be located, with all relations it comprises
      * @param rootNode, the parent node, underneath which the specified entity is expected to be found
      * */
    def find(entity: T, rootNode: Node): T =
        ???

}
