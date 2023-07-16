package io.github.rpiotrow.scenarios

import cats.effect.unsafe.implicits.global
import io.circe.Decoder
import io.circe.parser.parse
import io.github.rpiotrow.Settings
import sttp.client3.{HttpClientSyncBackend, UriContext, basicRequest}

object ScenariosSetup:
  def fetchCompaniesIds: List[String] =
    val request = basicRequest.get(uri"${Settings.baseUrl}/api?query=$companiesIdsRequest")
    val response = request.send(backend)
    (for
      body <- response.body
      json <- parse(body)
      ids <- json.hcursor.downField("data").downField("companies").as[List[CompanyIdResult]]
    yield ids.map(_.id)).left.map(msg => throw new RuntimeException(s"cannot fetch companies ids: $msg")).merge

  private val companiesIdsRequest = "{ companies { id } }"

  private case class CompanyIdResult(id: String)
  private given Decoder[CompanyIdResult] = Decoder.instance { hCursor =>
    hCursor.downField("id").as[String].map(CompanyIdResult.apply)
  }

  private val backend = HttpClientSyncBackend()
