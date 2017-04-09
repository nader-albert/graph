package na

import na.services.ContractService

object Runner extends App {

    val service = new ContractService

    println("nader is here !")
    service.getOne(3)

}
