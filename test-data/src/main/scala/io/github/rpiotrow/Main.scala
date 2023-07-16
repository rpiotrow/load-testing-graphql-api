package io.github.rpiotrow

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits.*
import doobie.*
import io.github.rpiotrow.db.Repository
import io.github.rpiotrow.mockaroo.CompanyListFetcher
import io.github.rpiotrow.service.CompanyService
import sttp.client3.httpclient.cats.HttpClientCatsBackend

object Main extends IOApp:

  private def repository =
    val xa = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:companies",
      user = "postgres",
      pass = "postgres"
    )
    new Repository(xa)

  def run(args: List[String]): IO[ExitCode] =
    val repository = Main.repository
    val companyService = CompanyService(repository)
    val numberOfRounds = 50

    HttpClientCatsBackend.resource[IO]().use { client =>
      List
        .range(0, numberOfRounds)
        .map { count =>
          for
            _ <- IO(println(s"Fetching and storing companies. Step $count/$numberOfRounds"))
            data <- CompanyListFetcher.fetch(client)
            _ <- companyService.storeCompanyEmployeesAndProjects(data)
          yield ExitCode.Success
        }
        .sequence
        .as(ExitCode.Success)
    }
