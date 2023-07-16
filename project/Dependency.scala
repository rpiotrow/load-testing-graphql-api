import sbt.*

// format: off
object Dependency {
  private object Version {
    val circe       = "0.15.0-M1"
    val doobie      = "1.0.0-RC2"
    val ducktape    = "0.1.10"
    val gatling     = "3.9.1"
    val grackle     = "0.12.0"
    val http4s      = "1.0.0-M35"
    val logback     = "1.4.8"
    val postgreSQL  = "42.5.4"
    val scalaCheck  = "1.17.0"
    val sttp        = "3.8.15"
  }

  val circeGeneric          = "io.circe"                       %% "circe-generic"             % Version.circe
  val circeParser           = "io.circe"                       %% "circe-parser"              % Version.circe
  val doobieCore            = "org.tpolecat"                   %% "doobie-core"               % Version.doobie
  val doobieHikari          = "org.tpolecat"                   %% "doobie-hikari"             % Version.doobie
  val doobiePostgres        = "org.tpolecat"                   %% "doobie-postgres"           % Version.doobie
  val ducktape              = "io.github.arainko"              %% "ducktape"                  % Version.ducktape
  val gatlingTestFramework  = "io.gatling"                     % "gatling-test-framework"     % Version.gatling
  val gatlingHighcharts     = "io.gatling.highcharts"          % "gatling-charts-highcharts"  % Version.gatling
  val grackleCore           = "edu.gemini"                     %% "gsp-graphql-core"          % Version.grackle
  val grackleDoobie         = "edu.gemini"                     %% "gsp-graphql-doobie-pg"     % Version.grackle
  val grackleGeneric        = "edu.gemini"                     %% "gsp-graphql-generic"       % Version.grackle
  val http4sBlazeServer     = "org.http4s"                     %% "http4s-blaze-server"       % Version.http4s
  val http4sDsl             = "org.http4s"                     %% "http4s-dsl"                % Version.http4s
  val http4sCirce           = "org.http4s"                     %% "http4s-circe"              % Version.http4s
  val logback               = "ch.qos.logback"                 %  "logback-classic"           % Version.logback
  val postgreSQL            = "org.postgresql"                 %  "postgresql"                % Version.postgreSQL
  val scalaCheck            = "org.scalacheck"                 %% "scalacheck"                % Version.scalaCheck
  val sttp                  = "com.softwaremill.sttp.client3"  %% "cats"                      % Version.sttp
}
