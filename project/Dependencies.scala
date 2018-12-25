import sbt._

object Dependencies {
  val catsVersion = "1.4.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val nsq = "com.github.mitallast" %% "scala-nsq" % "1.12"
  lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
  lazy val mockito = "org.mockito" % "mockito-all" % "1.10.19"
  lazy val deadbolt = "be.objectify" %% "deadbolt-scala" % "2.6.1"
  lazy val google = "com.github.seratch" %% "bigquery4s" % "0.9"
  lazy val awssdk2 = "software.amazon.awssdk" % "aws-sdk-java" % "2.1.3"

  lazy val bootstrap_b3 = "com.adrianhurt" %% "play-bootstrap" % "1.4-P26-B3-SNAPSHOT"
  lazy val bootstrap = "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery")
  lazy val jquery = "org.webjars" % "jquery" % "3.3.1-1"
  lazy val font_awesome = "org.webjars" % "font-awesome" % "4.7.0"
  lazy val datepicker = "org.webjars" % "bootstrap-datepicker" % "1.4.0" exclude("org.webjars", "bootstrap")
  lazy val resumablejs = "org.webjars" % "resumable.js" % "1.1.0"
}
