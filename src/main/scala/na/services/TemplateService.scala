package na.services

trait TemplateService [T] {

    def add(template: T)

    def getOne(template: T): T

    //TODO: do we need this here ? assuming EntityService is now dealing with Templates
    //def update(entity: T)

    //def delete(template: T)

}
