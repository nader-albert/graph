package na.repositories

trait RelationalRepository[T] {

    //1- Finds an entity with the specified Id in the database
    def findOne(id: Long): T = ???

}
