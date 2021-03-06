package na.services

import na.models.contracts.{Contract, ContractRevision}
import na.models.neo4j.RelTypes
import na.repositories.contracts.ContractRepository

class ContractService extends TemplateService[Contract] with VersioningService[Contract, ContractRevision]{

    override def add(entity: Contract): Unit = {
        ContractRepository.add(entity)
    }

   // override def delete(entity: Contract): Unit = ???

    override def getOne(template: Contract): Contract = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        ///findInGDM(findInRDM(template.id))
        ???
    }

    override def getLatest(template: Contract): Option[ContractRevision] = {
        //1- locate contract with all relations in graph ... contract returns with a valid package
        //2- enrich contract with supportive data from RDBMS

        //findInGDM(findInRDM(id))

        // Get the first Revision attached to this contract -if any-
        val firstRevision = ContractRepository.find(template, RelTypes.HAS_A.name())

        if(firstRevision.isDefined) {
            val lastRevision = ContractRepository.find(firstRevision.get, RelTypes.NEXT.name())

            if(lastRevision.isDefined) lastRevision else firstRevision
        }
        else
           None
    }

    //override def synchronise(entity: Contract): Contract = {
        // Updates the graph structure with new links that connect the given contract with the latest version of all its composites recursively
        // Returns another contract model that encapsulates the latest versions of al its composites.
    //}

    //override def update(entity: Contract): Unit = ???


    //override def delete(entity: ContractRevision): ContractRevision = ???

    override def getOriginal(template: Contract): ContractRevision = {
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

    /**
      * adds a new revision to the given template, and advances the current version to the given one
      * //TODO: should be done in one transaction
      **/
    override def addRevision(revision: ContractRevision): Unit = {
        ContractRepository.add(revision)

        // Try to get the latest version attached with enclosed contract, if found attach the current version to it as the next version,
        // if not attach the current version to parent contract

        val currentVersion = getLatest(revision.contract)

        if(currentVersion.isDefined) {
            ContractRepository.attach(currentVersion.get, revision)
        }
        else {
            ContractRepository.attach(revision.contract, revision)
        }
    }

    def addPackage(revision: ContractRevision): Unit = {
        revision.documentsPackage.foreach(ContractRepository.attach(revision,_))
    }

    /** *
      * retrieves the next revision after the current one, for the given template
      * */
    override def getNext(template: Contract): ContractRevision = ???

    /** *
      * retrieves the next revision after the given one, for the given template
      * */
    override def getNext(template: Contract, revision: ContractRevision): ContractRevision = ???

    /** *
      * retrieves the previous revision after the current one
      * */
    override def getPrevious(template: Contract): ContractRevision = ???

    /** *
      * retrieves the previous revision after the given one, for the given template
      * */
    override def getPrevious(template: Contract, revision: ContractRevision): ContractRevision = ???

    /**
      * restores a previously deleted revision
      **/
    override def restore(revision: ContractRevision): ContractRevision = ???

    /** *
      * resets the head of the template to the specified revision
      * */
    override def reset(template: Contract, revision: ContractRevision): ContractRevision = ???

    override def synchronise(entity: ContractRevision): ContractRevision = ???

}
