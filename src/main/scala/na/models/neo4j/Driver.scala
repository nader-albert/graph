package na.models.neo4j

import org.neo4j.driver.v1.AuthTokens
import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.driver.v1.Values.parameters

class Driver {

    private val driver = GraphDatabase.driver("bolt://localhost:7687" /*, AuthTokens.basic("neo4j", "neo4j")*/)

    try {
        val session = driver.session
        try {
            try {
                val tx = session.beginTransaction
                try {
                    tx.run("CREATE (a:Person {name: {name}, title: {title}})", parameters("name", "Arthur", "title", "King"))
                    tx.success
                } finally if (tx != null) tx.close()
            }
            try {
                val tx = session.beginTransaction
                try {
                    val result = tx.run("MATCH (a:Person) WHERE a.name = {name} " + "RETURN a.name AS name, a.title AS title", parameters("name", "Arthur"))
                    while ( {
                        result.hasNext
                    }) {
                        val record = result.next
                        System.out.println(String.format("%s %s", record.get("title").asString, record.get("name").asString))
                    }
                } finally if (tx != null) tx.close()
            }
        } finally if (session != null) session.close()
    }
}
