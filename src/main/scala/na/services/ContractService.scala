package na.services

import na.models.Contract
import na.repositories.contract.ContractRepository

import ContractRepository._

class ContractService extends EntityService[Contract] with VersioningService[Contract]{

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

    //override def synchronise(entity: Contract): Contract = {
        // Updates the graph structure with new links that connect the given contract with the latest version of all its composites recursively
        // Returns another contract model that encapsulates the latest versions of al its composites.
    //}

    override def update(entity: Contract): Unit = ???

    override def delete(entity: Contract): Unit = ???

    override def getOriginal(id: Long): Contract = {
      // assuming the ID here refers to the contract template instance (that doesn't hold any versioning specific information)
      // this assumes that a template will be created with the first version of an entity,

      // Node(Contract_For_Candidate_1)
      //     -> Node(ContractV1)
      //        -> Node(ContractV2)
      //            -> Node(ContractV3)
      //

      // using the ID, locate a unique contract node in the graph
      ???
    }

    override def getNext(id: Long): Contract = ???

    override def getPrevious(id: Long): Contract = ???

    override def delete(id: Long): Contract = ???

    override def restore(id: Long): Contract = ???

    override def synchronise(entity: Contract): Contract = ???
}
