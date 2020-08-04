
// see http://www.scala-sbt.org/0.13/docs/Parallel-Execution.html for details
concurrentRestrictions in Global := Seq(
  Tags.limit(Tags.Test, 1)
)

/*
 * MODULES
 ********************************************************/

lazy val avatarServiceClient = (project in file("."))
  .settings(
    name := "ubirch-avatar-service-client",
    description := "REST client for the avatarService",
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    scalaVersion := "2.11.12",
    scalacOptions ++= Seq("-feature"),
    organization := "com.ubirch.avatar",
    homepage := Some(url("http://ubirch.com")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/ubirch/ubirch-avatar-service"),
      "scm:git:git@github.com:ubirch/ubirch-avatar-service.git"
    )),
    version := "0.6.6-SNAPSHOT",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD", "-u", "xml-test-reports"),
    (sys.env.get("CLOUDREPO_USER"), sys.env.get("CLOUDREPO_PW")) match {
      case (Some(username), Some(password)) =>
        println("USERNAME and/or PASSWORD found.")
        credentials += Credentials("ubirch.mycloudrepo.io", "ubirch.mycloudrepo.io", username, password)
      case _ =>
        println("USERNAME and/or PASSWORD is taken from /.sbt/.credentials.")
        credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
    },
    publishTo := Some("io.cloudrepo" at "https://ubirch.mycloudrepo.io/repositories/trackle-mvn"),
    publishMavenStyle := true,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      resolverTrackle
    ),
    Seq(packagedArtifacts := Map.empty),
    libraryDependencies ++= depClientRest
  )


lazy val depClientRest = Seq(
  akkaHttp,
  akkaStream,
  akkaSlf4j,
  ubirchConfig,
  ubirchCrypto,
  ubirchResponse,
  ubirchDeepCheckModel,
  scalatest % "test",
  ubirchUUID

) ++ scalaLogging


/*
 * DEPENDENCIES
 ********************************************************/

// VERSIONS
val akkaV = "2.5.21"
val akkaHttpV = "10.1.3"
val scalaTestV = "3.0.5"
val logbackV = "1.2.3"
val logstashEncV = "5.0"
val slf4jV = "1.7.25"
val log4jV = "2.9.1"
val scalaLogV = "3.9.0"
val scalaLogSLF4JV = "2.1.2"

// GROUP NAMES
val akkaG = "com.typesafe.akka"
val logbackG = "ch.qos.logback"
val ubirchUtilG = "com.ubirch.util"

val scalaLogging = Seq(
  "org.slf4j" % "slf4j-api" % slf4jV,
  "org.slf4j" % "log4j-over-slf4j" % slf4jV,
  "org.slf4j" % "jul-to-slf4j" % slf4jV,
  "org.apache.logging.log4j" % "log4j-to-slf4j" % "2.11.0",
  "ch.qos.logback" % "logback-core" % logbackV,
  "ch.qos.logback" % "logback-classic" % logbackV,
  "net.logstash.logback" % "logstash-logback-encoder" % logstashEncV,
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % scalaLogSLF4JV,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLogV
)

val excludedLoggers = Seq(
  ExclusionRule(organization = "com.typesafe.scala-logging"),
  ExclusionRule(organization = "org.slf4j"),
  ExclusionRule(organization = "ch.qos.logback"),
  ExclusionRule(organization = "org.apache.logging")
)

val akkaStream = akkaG %% "akka-stream" % akkaV
val akkaSlf4j = akkaG %% "akka-slf4j" % akkaV
val akkaHttp = akkaG %% "akka-http" % akkaHttpV
val scalatest = "org.scalatest" %% "scalatest" % scalaTestV
val ubirchConfig = ubirchUtilG %% "config" % "0.2.3" excludeAll (excludedLoggers: _*)
val ubirchCrypto = ubirchUtilG %% "crypto" % "0.5.2" excludeAll (excludedLoggers: _*)
val ubirchDeepCheckModel = ubirchUtilG %% "deep-check-model" % "0.4.0" excludeAll (excludedLoggers: _*)
val ubirchResponse = ubirchUtilG %% "response-util" % "0.5.0" excludeAll (excludedLoggers: _*)
val ubirchUUID = ubirchUtilG %% "uuid" % "0.1.3" excludeAll (excludedLoggers: _*)

/*
 * RESOLVER
 ********************************************************/

lazy val resolverTrackle = "ubirch.mycloudrepo.io" at "https://ubirch.mycloudrepo.io/repositories/trackle-mvn"
/*
 * MISC
 ********************************************************/
