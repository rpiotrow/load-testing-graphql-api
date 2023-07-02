package io.github.rpiotrow.postgres

case class PostgresConnectionInfo(host: String, port: Int) {
  val driverClassName = "org.postgresql.Driver"
  val databaseName = "companies"
  val jdbcUrl = s"jdbc:postgresql://$host:$port/$databaseName"
  val username = "postgres"
  val password = "postgres"
}

object PostgresConnectionInfo {
  val local = PostgresConnectionInfo(host = "localhost", port = 5432)
}
