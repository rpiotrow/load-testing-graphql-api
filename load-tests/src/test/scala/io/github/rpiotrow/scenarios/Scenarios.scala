package io.github.rpiotrow.scenarios

import io.circe.syntax.*
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.github.rpiotrow.graphql.GraphQLQueries
import io.github.rpiotrow.graphql.QueriesGens.companiesQueries
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Scenarios:

  private val testHeaders = Map(
    "Accept" -> "application/json; charset=utf-8",
    "Content-Type" -> "application/json"
  )

  private lazy val companiesGraphQLQueryAsString: Iterator[Map[String, String]] =
    Iterator.continually {
      val query = companiesQueries.pureApply(Gen.Parameters.default, Seed.random())
      Map("queryJson" -> GraphQLQueries.from(query).asJson.noSpaces)
    }

  lazy val companiesGraphQLQuery: ScenarioBuilder =
    scenario("Companies GraphQL query")
      .feed(companiesGraphQLQueryAsString)
      .exec {
        http("Full GraphQL query")
          .post("/api")
          .body(StringBody("${queryJson}"))
          .headers(testHeaders)
          .check(status.is(200))
      }
