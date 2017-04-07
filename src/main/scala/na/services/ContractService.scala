package na.services

import na.models.Contract
import na.repositories.contract.ContractRepository

import ContractRepository._

class ContractService extends EntityService[Contract] with VersioningService [Contract]{

    override def add(entity: Contract): Unit = {
        //
        ???
    }

    override def getOne(id: Long): Contract = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        findInGDM(findInRDM(id))
    }

    override def getLatest(id: Long): Contract = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        findInGDM(findInRDM(id))
    }

    override def getOriginal(id: Long): Contract = {
        //Go

    }

    override def synchronise(entity: Contract): Contract = {
        // Updates the graph structure with new links that connect the given contract with the latest version of all its composites recursively
        // Returns another contract model that encapsulates the latest versions of al its composites.
    }

    override def update(entity: Contract): Unit = ???

    override def delete(entity: Contract): Unit = ???
}
