package na.services

trait VersioningService [T] {
    def getLatest(id: Long): T

    def getOriginal(id: Long): T

    def synchronise(entity: T): T

}
