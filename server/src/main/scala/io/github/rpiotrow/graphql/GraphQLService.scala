package io.github.rpiotrow.graphql

import cats.effect.Concurrent
import cats.implicits.*
import edu.gemini.grackle.Mapping
import io.circe.{Json, ParsingFailure, parser}
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, InvalidMessageBodyFailure, ParseFailure, QueryParamDecoder}

trait GraphQLService[F[_]]:
  def runQuery(op: Option[String], vars: Option[Json], query: String): F[Json]

object GraphQLService:

  def fromMapping[F[_]: Concurrent](mapping: Mapping[F]): GraphQLService[F] =
    (op: Option[String], vars: Option[Json], query: String) => mapping.compileAndRun(query, op, vars)

  def routes[F[_]: Concurrent](prefix: String, service: GraphQLService[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*

    implicit val jsonQPDecoder: QueryParamDecoder[Json] = QueryParamDecoder[String].emap { s =>
      parser.parse(s).leftMap { case ParsingFailure(msg, _) => ParseFailure("Invalid variables", msg) }
    }

    object QueryMatcher extends QueryParamDecoderMatcher[String]("query")
    object OperationNameMatcher extends OptionalQueryParamDecoderMatcher[String]("operationName")
    object VariablesMatcher extends OptionalValidatingQueryParamDecoderMatcher[Json]("variables")

    HttpRoutes.of[F] {
      // GraphQL query is embedded in the URI query string when queried via GET
      case GET -> Root / `prefix` :? QueryMatcher(query) +& OperationNameMatcher(op) +& VariablesMatcher(vars0) =>
        vars0.sequence.fold(
          errors => BadRequest(errors.map(_.sanitized).mkString_("", ",", "")),
          vars =>
            for
              result <- service.runQuery(op, vars, query)
              resp <- Ok(result)
            yield resp
        )

      // GraphQL query is embedded in a Json request body when queried via POST
      case req @ POST -> Root / `prefix` =>
        for
          body <- req.as[Json]
          obj <- body.asObject.liftTo[F](InvalidMessageBodyFailure("Invalid GraphQL query"))
          query <- obj("query").flatMap(_.asString).liftTo[F](InvalidMessageBodyFailure("Missing query field"))
          op = obj("operationName").flatMap(_.asString)
          vars = obj("variables")
          result <- service.runQuery(op, vars, query)
          resp <- Ok(result)
        yield resp
    }
