package io.github.rpiotrow.simulations

import io.gatling.core.Predef.*
import io.gatling.http.Predef.http
import io.github.rpiotrow.Settings
import io.github.rpiotrow.scenarios.Scenarios

import scala.concurrent.duration.*
import scala.util.Properties

class CompaniesGraphQLQuerySimulation extends Simulation {

  private val httpConf = http
    .baseUrl(Settings.baseUrl)
    .shareConnections

  setUp(
    Scenarios.companiesGraphQLQuery.inject(
      (rampUsersPerSec(0) to 2).during(30.seconds),
      constantUsersPerSec(2).during(30.seconds),
      (rampUsersPerSec(2) to 0).during(30.seconds)
    )
  )
    .protocols(httpConf)
    .assertions(
      global.responseTime.percentile2.lt(5000),
      global.responseTime.max.lt(10000),
      global.failedRequests.percent.lt(5)
    )
}
