package io.github.rpiotrow.mockaroo

import cats.effect.IO
import cats.implicits.given
import io.circe.parser.*
import io.circe.syntax.*
import io.github.rpiotrow.mockaroo.JsonCodecs.given
import io.github.rpiotrow.mockaroo.Model.Company
import sttp.capabilities.WebSockets
import sttp.client3.{SttpBackend, UriContext, basicRequest}

object CompanyListFetcher:

  private val mockarooApiKey = scala.util.Properties.envOrElse("MOCKAROO_API_KEY", "<put-your-mockaroo-api-key-to-env-variable>")

  def fetch(client: SttpBackend[IO, WebSockets]): IO[List[Company]] =
    for
      response <- client.send(
        basicRequest
          .get(uri"https://api.mockaroo.com/api/e11c4970?count=10")
          .header("X-API-Key", mockarooApiKey)
      )
      body <- IO.fromEither(response.body.left.map(msg => new RuntimeException(msg)))
      json <- IO.fromEither(parse(body))
      data <- IO.fromEither(json.as[List[Company]])
    yield data
