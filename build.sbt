name := "simple-chat-app"

organization := "com.simple_chat"

scalaVersion := "2.10.4"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.4"

libraryDependencies += "org.scala-lang" % "scala-actors" % "2.10.4"

// Amazon Web Services
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.3.20" withSources()

// Apache Camel Services
libraryDependencies ++= Seq("org.apache.camel" % "camel-core" % "2.9.2",
  "org.apache.camel" % "camel-aws" % "2.10.4")