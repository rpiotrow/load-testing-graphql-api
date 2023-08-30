package io.github.rpiotrow.domain

import doobie.Meta
import io.circe.Encoder

enum ProjectStatus:
  case New, InProgress, Completed, Cancelled

object ProjectStatus:
  given Encoder[ProjectStatus] = Encoder.encodeString.contramap(_.toString)
  given Meta[ProjectStatus] =
    Meta[String].timap {
      case "New"        => New
      case "InProgress" => InProgress
      case "Completed"  => Completed
      case "Cancelled"  => Cancelled
    } { _.toString }
