// scalastyle:off

//import com.typesafe.sbt.packager.SettingsHelper._

// *** Settings ***

useGpg := false

lazy val commonSettings = Seq(
  organization := "org.scala-rules",
  organizationHomepage := Some(url("https://github.com/scala-rules")),
  homepage := Some(url("https://github.com/scala-rules/examples")),
  version := "1.0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-Xfatal-warnings")
) ++ staticAnalysisSettings ++ publishSettings


// *** Projects ***

lazy val ruleViewerRoot = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "examples",
    description := "DSL Examples to use during development of rule viewer",
    libraryDependencies ++= dependencies
  )

// *** Dependencies ***

lazy val scalaRulesVersion = "0.3.4"
lazy val scalaTestVersion = "2.2.5"
lazy val jodaTimeVersion = "2.4"
lazy val jodaConvertVersion = "1.8"

lazy val dependencies = Seq(
  "org.scala-rules" %% "rule-engine" % scalaRulesVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2",
  "com.fasterxml.jackson.jaxrs" % "jackson-jaxrs-json-provider" % "2.7.3",
  "joda-time" % "joda-time" % jodaTimeVersion,
  "org.joda" % "joda-convert" % jodaConvertVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.5" % Test,
  "com.storm-enroute" %% "scalameter" % "0.7" % Test,
  "org.scala-rules" %% "finance-dsl" % scalaRulesVersion,
  "org.scala-rules" %% "rule-engine-test-utils" % scalaRulesVersion % Test
)


// *** Static analysis ***

lazy val staticAnalysisSettings = {
  lazy val compileScalastyle = taskKey[Unit]("Runs Scalastyle on production code")
  lazy val testScalastyle = taskKey[Unit]("Runs Scalastyle on test code")

  Seq(
    scalastyleConfig in Compile := (baseDirectory in ThisBuild).value / "project" / "scalastyle-config.xml",
    scalastyleConfig in Test := (baseDirectory in ThisBuild).value / "project" / "scalastyle-test-config.xml",

    // The line below is needed until this issue is fixed: https://github.com/scalastyle/scalastyle-sbt-plugin/issues/44
    scalastyleConfig in scalastyle := (baseDirectory in ThisBuild).value / "project" / "scalastyle-test-config.xml",

    compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
    testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
  )
}

addCommandAlias("verify", ";compileScalastyle;testScalastyle;coverage;test;coverageReport;coverageAggregate")

// *** Publishing ***

lazy val publishSettings = Seq(
  pomExtra := pom,
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  }
)

lazy val pom =
  <developers>
    <developer>
      <name>Vincent Zorge</name>
      <email>scala-rules@linuse.nl</email>
      <organization>Linuse</organization>
      <organizationUrl>https://github.com/vzorge</organizationUrl>
    </developer>
    <developer>
      <name>Jan-Hendrik Kuperus</name>
      <email>jan-hendrik@scala-rules.org</email>
      <organization>Yoink Development</organization>
      <organizationUrl>http://www.yoink.nl</organizationUrl>
    </developer>
    <developer>
      <name>Nathan Perdijk</name>
      <email>nathan@scala-rules.org</email>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:git@github.com:scala-rules/examples.git</connection>
    <developerConnection>scm:git:git@github.com:scala-rules/examples.git</developerConnection>
    <url>git@github.com:scala-rules/examples.git</url>
  </scm>
  
