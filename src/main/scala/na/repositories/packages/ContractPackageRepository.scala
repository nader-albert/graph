package na.repositories.packages

import na.models.neo4j.RelTypes
import na.models.packages.{ContractPackage, ContractPackageRevision}
import na.repositories.{GraphRepository, RelationalRepository}
import org.neo4j.driver.v1.Values.parameters

object ContractPackageRepository extends RelationalRepository[ContractPackageRevision] with GraphRepository[ContractPackage, ContractPackageRevision]{

    override val templateAlias = "package"
    override val reversionAlias = "pkRevision"

    /**
      * creates a new template from the specified type
      **/
    override def add(templatePackage: ContractPackage): Unit = {
        val alias = templatePackage.name
        val contractPackageLabel = templatePackage.typeName
        val packageId = "uuid: {uuid}"
        val packageName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + contractPackageLabel + " {" + packageId + ", " + packageName + "})"
                , parameters("uuid", templatePackage.uuid.toString , "name", templatePackage.name) )
        }
    }

    /**
      * creates a new template from the specified type
      **/
    override def add(revision: ContractPackageRevision): Unit = {
        val alias = revision.name
        val revisionLabel = ContractPackageRevision.typeName
        val contractId = "uuid: {uuid}"
        val contractName = "name: {name}"

        execute {
            ("CREATE (" + alias + ":" + revisionLabel + " {" + contractId + ", " + contractName + " })"
                ,
                parameters(
                    "uuid", revision.uuid.toString ,
                    "name", revision.name) )
        }
    }

    /**
      * creates a new version and link it with the given template
      **/
    override def attach(templatePackage: ContractPackage, revision: ContractPackageRevision): ContractPackageRevision = {
        execute {
            (MATCH(one(templatePackage) and one(revision)) andThen CREATE(link(RelTypes.HAS_A.name())),

                parameters(
                    "contractName", templatePackage.name,
                    "contractUuid", templatePackage.uuid.toString,
                    "pkRevisionUuid", revision.uuid.toString,
                    "pkRevisionName", revision.name))
        }

        revision
    }

    override def find(template: ContractPackage): ContractPackageRevision = ???

    def one(contract :ContractPackage): String = "(" + templateAlias + ":" + contract.typeName + "{name:{contractName}, uuid:{contractUuid} } )"

    def one(contractRevision: ContractPackageRevision): String =
        "(" + reversionAlias + ":" + ContractPackageRevision.typeName + "{name:{pkRevisionName}, uuid:{pkRevisionUuid} } )"

    def link(connectionName: String): String = "(" + templateAlias + ")-[r:" + connectionName + "]->(" + reversionAlias + ")"

}
