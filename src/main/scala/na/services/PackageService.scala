package na.services

import na.models.PackageTemplate

class PackageService extends EntityService[PackageTemplate] with VersioningService[Package] {

  override def add(entity: PackageTemplate): Unit = ???

  override def getOne(id: Long): Package = ???

  //override def update(entity: PackageTemplate): Unit = ???

  override def delete(entity: PackageTemplate): Unit = ???

  override def addRevision(): Unit = ???

  override def getLatest(id: Long): Package = ???

  override def getOriginal(id: Long): Package = ???

  override def getNext(id: Long): Package = ???

  override def getPrevious(id: Long): Package = ???

  override def delete(id: Long): Package = ???

  override def restore(id: Long): Package = ???

  override def synchronise(entity: Package): Package = ???

}
