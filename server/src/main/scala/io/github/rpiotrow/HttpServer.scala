package io.github.rpiotrow

import cats.effect.Async
import cats.implicits.*
import com.comcast.ip4s.*
import fs2.Stream
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent.resourceServiceBuilder
import org.typelevel.log4cats.LoggerFactory

object HttpServer:
  def run[F[_]: Async: Network: LoggerFactory](graphQLRoutes: HttpRoutes[F]): F[Unit] =
    for
      // Routes for static resources, i.e. GraphQL Playground
      assetRoutes <- resourceServiceBuilder[F]("/assets").toRoutes
      // adding GraphQL routes
      routes = (assetRoutes <+> graphQLRoutes).orNotFound
      // request and response logger
      httpApp = Logger.httpApp[F](true, false)(routes)
      // Spin up the server ...
      _ <- EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(httpApp)
        .build
        .useForever
    yield ()
