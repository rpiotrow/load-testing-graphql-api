package io.github.rpiotrow.scenarios

import io.gatling.core.Predef.*
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*

object Scenarios {

  private val testHeaders = Map(
    "Accept" -> "application/json; charset=utf-8"
  )

  lazy val fullQuery: ScenarioBuilder =
    scenario("Full GraphQL query")
      .exec {
        http("Full GraphQL query")
          .get(s"/api?${ScenariosSetup.fullQuery}")
          .headers(testHeaders)
          .check(status.is(200))
      }
}
