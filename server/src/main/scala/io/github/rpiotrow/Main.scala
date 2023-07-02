package io.github.rpiotrow

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.*
import doobie.hikari.HikariTransactor
import io.github.rpiotrow.graphql.{CompaniesMapping, GraphQLService}
import io.github.rpiotrow.postgres.PostgresConnectionInfo

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.*

object Main extends IOApp:
  def run(args: List[String]): IO[ExitCode] =
    val connectionInfo = PostgresConnectionInfo.local
    transactor(connectionInfo)
      .use { xa =>
        val worldGraphQLRoutes = GraphQLService.routes(
          "api",
          GraphQLService.fromMapping(CompaniesMapping.mkMappingFromTransactor(xa))
        )
        DemoServer.stream[IO](worldGraphQLRoutes).compile.drain
      }
      .as(ExitCode.Success)

  private def transactor(connInfo: PostgresConnectionInfo): Resource[IO, HikariTransactor[IO]] =
    import connInfo.*
    HikariTransactor.newHikariTransactor[IO](
      driverClassName,
      jdbcUrl,
      username,
      password,
      ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))
    )
