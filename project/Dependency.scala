import sbt.*

// format: off
object Dependency {
  private object Version {
    val gatling               = "3.9.1"
    val grackle               = "0.12.0"
    val http4s                = "1.0.0-M35"
    val postgreSQL            = "42.5.4"
  }

  val gatlingTestFramework   = "io.gatling"                 % "gatling-test-framework"       % Version.gatling
  val gatlingHighcharts      = "io.gatling.highcharts"      % "gatling-charts-highcharts"    % Version.gatling
  val grackleCore            = "edu.gemini"                 %% "gsp-graphql-core"            % Version.grackle
  val grackleDoobie          = "edu.gemini"                 %% "gsp-graphql-doobie-pg"       % Version.grackle
  val grackleGeneric         = "edu.gemini"                 %% "gsp-graphql-generic"         % Version.grackle
  val http4sBlazeServer      = "org.http4s"                 %% "http4s-blaze-server"         % Version.http4s
  val http4sDsl              = "org.http4s"                 %% "http4s-dsl"                  % Version.http4s
  val http4sCirce            = "org.http4s"                 %% "http4s-circe"                % Version.http4s
  val postgreSQL             = "org.postgresql"             %  "postgresql"                  % Version.postgreSQL
}