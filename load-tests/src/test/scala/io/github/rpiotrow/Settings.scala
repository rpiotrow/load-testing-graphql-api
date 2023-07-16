package io.github.rpiotrow

import scala.util.Properties

object Settings:
  val baseUrl: String = Properties.envOrElse("TARGET_ADDRESS", "http://localhost:8080")
