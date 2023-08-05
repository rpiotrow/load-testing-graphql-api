package io.github.rpiotrow.simulations

import io.gatling.core.Predef.*
import io.gatling.http.Predef.http
import io.github.rpiotrow.Settings
import io.github.rpiotrow.scenarios.Scenarios

import scala.concurrent.duration.*
import scala.util.Properties

class CompanyGraphQLQuerySimulation extends Simulation {

  private val httpConf = http
    .baseUrl(Settings.baseUrl)
    .shareConnections

  setUp(
    Scenarios.companyGraphQLQuery.inject(
      (rampUsersPerSec(0) to 20).during(30.seconds),
      constantUsersPerSec(20).during(30.seconds),
      (rampUsersPerSec(20) to 0).during(30.seconds)
    )
  )
    .protocols(httpConf)
    .assertions(
      global.responseTime.percentile3.lt(3000),
      global.responseTime.max.lt(5000),
      global.failedRequests.percent.lt(5)
    )
}
