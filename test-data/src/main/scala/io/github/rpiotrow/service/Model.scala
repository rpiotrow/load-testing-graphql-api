package io.github.rpiotrow.service

import io.github.rpiotrow.domain.*
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
    socialMedia: SocialMedia,
    employees: List[Employee]
  )

  case class Employee(
    id: UUID,
    firstName: String,
    lastName: String,
    email: String,
    phone: Option[String],
    position: String,
    department: String,
    startDate: Instant,
    projects: List[Project]
  )
