package io.github.rpiotrow.simulations

import io.gatling.core.Predef.*
import io.gatling.http.Predef.http
import io.github.rpiotrow.scenarios.Scenarios

import scala.concurrent.duration.*
import scala.util.Properties

class FullGraphQLQuerySimulation extends Simulation {

  private val baseUrl: String = Properties.envOrElse("TARGET_ADDRESS", "http://localhost:8080")

  private val httpConf = http
    .baseUrl(baseUrl)
    .shareConnections

  setUp(
    Scenarios.fullQuery.inject(
      (rampUsersPerSec(0) to 20).during(30.seconds),
      constantUsersPerSec(20).during(30.seconds)
    )
  )
    .protocols(httpConf)
    .assertions(
      global.responseTime.percentile3.lt(3000),
      global.responseTime.max.lt(5000),
      global.failedRequests.percent.lt(5)
    )
}
