package io.github.rpiotrow.mockaroo

import io.circe.*
import io.circe.generic.semiauto.*
import io.github.rpiotrow.mockaroo.Model.*
import io.github.rpiotrow.domain.*

import java.util.UUID
import java.time.Instant
import java.time.temporal.ChronoUnit

object JsonCodecs:
  given Decoder[Location] = (c: HCursor) =>
    for
      address <- c.downField("address").as[String]
      postCode <- c.downField("postCode").as[Int]
      city <- c.downField("city").as[String]
      country <- c.downField("country").as[String]
    yield Location(address, postCode.toString, city, country)

  given Decoder[SocialMedia] = deriveDecoder[SocialMedia]
  given Decoder[Employee] = deriveDecoder[Employee]
  given Decoder[ProjectStatus] = Decoder.decodeString.emap {
    case "New"        => Right(ProjectStatus.New)
    case "InProgress" => Right(ProjectStatus.InProgress)
    case "Completed"  => Right(ProjectStatus.Completed)
    case "Cancelled"  => Right(ProjectStatus.Cancelled)
    case other        => Left(s"Unknown ProjectStatus: $other")
  }
  given Decoder[Project] = (c: HCursor) =>
    for
      id <- c.downField("id").as[UUID]
      name <- c.downField("name").as[String]
      description <- c.downField("description").as[String]
      startDate <- c.downField("startDate").as[Instant]
      duration <- c.downField("duration").as[Int]
      status <- c.downField("status").as[ProjectStatus]
      budget <- c.downField("budget").as[BigDecimal]
    yield Project(id, name, description, startDate, endDate = startDate.plus(duration, ChronoUnit.DAYS), status, budget)
  given Decoder[Company] = deriveDecoder[Company]
