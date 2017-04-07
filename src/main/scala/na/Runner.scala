package na

import na.services.ContractService

class Runner extends App {

    val service = new ContractService

    service.getOne(3)

}
