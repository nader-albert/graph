package na.services

trait EntityService [T] {

    def add(entity: T)

    def getOne(id: Long): T

    //TODO: do we need this here ? assuming EntityService is now dealing with Templates
    //def update(entity: T)

    def delete(entity: T)

}
