package io.github.rpiotrow.scenarios

import io.circe.syntax.*
import io.gatling.core.Predef.*
import io.gatling.core.feeder.FeederBuilderBase
import io.gatling.jdbc.Predef.*
import io.github.rpiotrow.graphql.GraphQLQueries
import io.github.rpiotrow.graphql.QueriesGens.{companiesQueries, companyQueries}
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

trait ScenariosSetup:
  protected val testHeaders: Map[String, String] = Map(
    "Accept" -> "application/json; charset=utf-8",
    "Content-Type" -> "application/json"
  )

  protected lazy val companiesGraphQLQueryAsString: Iterator[Map[String, String]] =
    Iterator.continually {
      val query = companiesQueries.pureApply(Gen.Parameters.default, Seed.random())
      //TODO: add paging options (pageNumber, itemsPerPage, orderBy)
      Map("queryJson" -> GraphQLQueries.from(query).asJson.noSpaces)
    }

  protected lazy val companyGraphQLQueryFeeder: FeederBuilderBase[Any] =
    val host = "localhost"
    val port = 5432
    val databaseName = "companies"
    jdbcFeeder(
      url = s"jdbc:postgresql://$host:$port/$databaseName",
      username = "postgres",
      password = "postgres",
      sql = "SELECT id as companyquery FROM companies"
    ).asInstanceOf[FeederBuilderBase[String]].transform { case (_, companyId) =>
      val query = companyQueries
        .pureApply(Gen.Parameters.default, Seed.random())
        .copy(id = companyId)
      GraphQLQueries.from(query).asJson.noSpaces
    }
