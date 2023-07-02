val scala3Version = "3.3.0"

enablePlugins(FlywayPlugin)

lazy val commonSettings = Seq(
  scalaVersion := scala3Version,
  version := "0.0.1-SNAPSHOT",
  organization := "io.github.rpiotrow",
  run / fork := true,
  publish / skip := true
)

lazy val loadTestingGraphqlApi =
  project
    .in(file("."))
    .settings(commonSettings*)
    .settings(
      flywayUrl := "jdbc:postgresql://localhost:5432/companies",
      flywayUser := "postgres",
      flywayPassword := "postgres",
      flywayLocations := Seq(s"filesystem:${baseDirectory.value}/db.migration"),
      libraryDependencies ++= Seq(
        Dependency.postgreSQL
      )
    )
    .aggregate(
      server,
      `load-tests`,
      `test-data`
    )

lazy val server =
  project
    .in(file("server"))
    .settings(commonSettings*)
    .settings(
      name := "server",
      libraryDependencies ++= Seq(
        Dependency.doobieCore,
        Dependency.doobieHikari,
        Dependency.doobiePostgres,
        Dependency.grackleCore,
        Dependency.grackleDoobie,
        Dependency.http4sBlazeServer,
        Dependency.http4sCirce,
        Dependency.http4sDsl,
        Dependency.logback,
        Dependency.postgreSQL
      )
    )

lazy val `load-tests` =
  project
    .in(file("load-tests"))
    .enablePlugins(GatlingPlugin)
    .settings(commonSettings*)
    .settings(
      name := "load-tests",
      libraryDependencies ++= Seq(
        Dependency.gatlingTestFramework % "test",
        Dependency.gatlingHighcharts % "test",
        Dependency.grackleCore % "test",
        Dependency.postgreSQL % "test",
        Dependency.scalaCheck % "test"
      )
    )

lazy val `test-data` =
  project
    .in(file("test-data"))
    .settings(commonSettings*)
    .settings(
      name := "test-data",
      libraryDependencies ++= Seq(
        Dependency.postgreSQL
      )
    )
