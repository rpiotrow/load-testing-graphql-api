package io.github.rpiotrow

import java.time.Instant
import java.util.UUID

object Model:

  case class Company(
    id: UUID,
    name: String,
    industry: String,
    location: Location,
    foundedYear: Int,
    website: Option[String],
    email: Option[String],
    phone: Option[String],
    socialMedia: SocialMedia
  )

  case class Location(
    address: String,
    postCode: String,
    city: String,
    country: String
  )

  case class SocialMedia(facebook: Option[String], instagram: Option[String], linkedIn: Option[String])

  case class Employee(
    id: UUID,
    firstName: String,
    lastName: String,
    email: String,
    phone: Option[String],
    position: String,
    department: String,
    startDate: Instant
  )

  case class Project(
    id: UUID,
    name: String,
    description: String,
    startDate: Instant,
    endDate: Instant,
    status: ProjectStatus,
    budget: BigDecimal
  )

  enum ProjectStatus:
    case New, InProgress, Completed, Cancelled
