name := "xmlCreator"

version := "1.0"

scalaVersion := "2.11.5"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml_2.11
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.6"
libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.8"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.8"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.8"
ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

