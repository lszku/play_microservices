import Dependencies._

lazy val root = (project in file(".")).enablePlugins(PlayScala).
  settings(
    inThisBuild(List(
      organization := "com.bomoda",
      scalaVersion := "2.12.7",
      version := "0.1.0"
    )),
    organization := "com.bomoda",
    name := "play_microservice",
    libraryDependencies ++= Seq(
      filters,
      nsq,
      jdbc,
      ehcache,
      ws,
      //      specs2 % Test,
      scalaTest % Test,
      guice,
      cats,
      mockito % Test,
      deadbolt,
      google,
      awssdk2,

      bootstrap,
      bootstrap_b3,
      jquery,
      font_awesome,
      datepicker,
      resumablejs,
      "com.pauldijou" %% "jwt-play" % "0.19.0",
      "com.pauldijou" %% "jwt-core" % "0.19.0",
      "com.auth0" % "jwks-rsa" % "0.6.1"
    ),
    resolvers ++= Seq(
      "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
      "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
      //     "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      Resolver.sonatypeRepo("snapshots")
    )
  )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.bomoda.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.bomoda.binders._"
