package io.github.rpiotrow.scenarios

import io.circe.syntax.*
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.github.rpiotrow.graphql.GraphQLQueries
import io.github.rpiotrow.graphql.QueriesGens.{companiesQueries, companyQueries}
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Scenarios:
  lazy val companiesGraphQLQuery: ScenarioBuilder =
    scenario("Companies list GraphQL query")
      .feed(companiesGraphQLQueryAsString)
      .exec {
        http("Companies list GraphQL query")
          .post("/api")
          .body(StringBody("${queryJson}"))
          .headers(testHeaders)
          .check(status.is(200))
          .check(jmesPath("errors").notExists)
          .check(jmesPath("data.companies").exists)
      }

  lazy val companyGraphQLQuery: ScenarioBuilder =
    scenario("Company GraphQL query")
      .feed(companyGraphQLQueryAsString.random)
      .exec {
        http("GraphQL query for episode")
          .post("/api")
          .body(StringBody("${queryJson}"))
          .headers(testHeaders)
          .check(status.is(200))
          .check(jmesPath("errors").notExists)
          .check(jmesPath("data.company").exists)
      }

  private val testHeaders = Map(
    "Accept" -> "application/json; charset=utf-8",
    "Content-Type" -> "application/json"
  )

  private lazy val companiesGraphQLQueryAsString: Iterator[Map[String, String]] =
    Iterator.continually {
      val query = companiesQueries.pureApply(Gen.Parameters.default, Seed.random())
      Map("queryJson" -> GraphQLQueries.from(query).asJson.noSpaces)
    }

  private lazy val companyGraphQLQueryAsString: Array[Map[String, String]] =
    ScenariosSetup.fetchCompaniesIds.map { companyId =>
      val query = companyQueries
        .pureApply(Gen.Parameters.default, Seed.random())
        .copy(id = companyId)
      Map("queryJson" -> GraphQLQueries.from(query).asJson.noSpaces)
    }.toArray
