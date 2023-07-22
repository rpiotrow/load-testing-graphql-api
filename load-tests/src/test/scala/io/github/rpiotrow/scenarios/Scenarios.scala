package io.github.rpiotrow.scenarios

import io.circe.syntax.*
import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*

object Scenarios extends ScenariosSetup:

  lazy val companiesGraphQLQuery: ScenarioBuilder =
    scenario("Companies list GraphQL query")
      .feed(companiesGraphQLQueryAsString)
      .exec {
        http("Companies list GraphQL query")
          .post("/api")
          .body(StringBody("#{queryJson}"))
          .headers(testHeaders)
          .check(status.is(200))
          .check(jmesPath("errors").notExists)
          .check(jmesPath("data.companies").exists)
      }

  lazy val companyGraphQLQuery: ScenarioBuilder =
    scenario("Company GraphQL query")
      .feed(companyGraphQLQueryFeeder.random)
      .exec {
        http("Company GraphQL query")
          .post("/api")
          .body(StringBody("#{companyquery}"))
          .headers(testHeaders)
          .check(status.is(200))
          .check(jmesPath("errors").notExists)
          .check(jmesPath("data.company").exists)
      }
