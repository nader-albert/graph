package na.services

import na.models.Contract
import na.repositories.contract.ContractRepository

import ContractRepository._

class ContractService extends EntityService [Contract]{

    override def add(entity: Contract): Unit = {
        //
        ???
    }

    override def getOne(id: Long): Contract = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        findInGDM(findInRDM(id))
    }

    override def update(entity: Contract): Unit = ???

    override def delete(entity: Contract): Unit = ???
}
