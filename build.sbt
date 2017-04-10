name := "neo4j-scala"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    "org.neo4j" % "neo4j" % "3.1.3",
    "org.neo4j.driver" % "neo4j-java-driver" % "1.2.0"
)
