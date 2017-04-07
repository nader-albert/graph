package na.services

trait EntityService [T] {

    def add(entity: T)

    def getOne(id: Long): T

    def update(entity: T)

    def delete(entity: T)

}
