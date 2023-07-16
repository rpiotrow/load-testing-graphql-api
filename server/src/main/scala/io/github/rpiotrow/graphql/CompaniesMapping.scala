package io.github.rpiotrow.graphql

import _root_.doobie.postgres.*
import _root_.doobie.postgres.implicits.*
import _root_.doobie.{Meta, Transactor}
import cats.effect.Sync
import cats.implicits.*
import edu.gemini.grackle.*
import edu.gemini.grackle.PathTerm.UniquePath
import edu.gemini.grackle.Predicate.*
import edu.gemini.grackle.Query.*
import edu.gemini.grackle.QueryCompiler.*
import edu.gemini.grackle.Value.*
import edu.gemini.grackle.doobie.postgres.{DoobieMapping, DoobieMonitor, LoggedDoobieMappingCompanion}
import edu.gemini.grackle.sql.Like
import edu.gemini.grackle.syntax.*
import io.circe.Encoder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.time.ZonedDateTime

//TODO: duplicate from model
enum ProjectStatus:
  case New, InProgress, Completed, Cancelled
object ProjectStatus:
  //TODO: move encoder to separate object for circe/json codecs
  given Encoder[ProjectStatus] = Encoder.encodeString.contramap(_.toString)
  //TODO: move doobie meta to separate object for doobie/database codecs
  given Meta[ProjectStatus] =
    Meta[String].timap {
      case "New" => New
      case "InProgress" => InProgress
      case "Completed" => Completed
      case "Cancelled" => Cancelled
    } { _.toString }

trait CompaniesMapping[F[_]] extends DoobieMapping[F]:

  object companies extends TableDef("companies"):
    val id = col("id", Meta[String])
    val name = col("name", Meta[String])
    val industry = col("industry", Meta[String])
    val locationAddress = col("location_address", Meta[String])
    val locationPostCode = col("location_post_code", Meta[String])
    val locationCity = col("location_city", Meta[String])
    val locationCountry = col("location_country", Meta[String])
    val foundedYear = col("founded_year", Meta[Int])
    val website = col("website", Meta[String], nullable = true)
    val email = col("email", Meta[String], nullable = true)
    val phone = col("phone", Meta[String], nullable = true)
    val socialMediaFacebook = col("social_media_facebook", Meta[String], nullable = true)
    val socialMediaInstagram = col("social_media_instagram", Meta[String], nullable = true)
    val socialMediaTwitter = col("social_media_twitter", Meta[String], nullable = true)
    val socialMediaMastodon = col("social_media_mastodon", Meta[String], nullable = true)
    val socialMediaLinkedIn = col("social_media_linked_in", Meta[String], nullable = true)

  object employees extends TableDef("employees"):
    val id = col("id", Meta[String])
    val companyId = col("company_id", Meta[String])
    val firstName = col("first_name", Meta[String])
    val lastName = col("last_Name", Meta[String])
    val email = col("email", Meta[String])
    val phone = col("phone", Meta[String], nullable = true)
    val position = col("position", Meta[String])
    val department = col("department", Meta[String])
    val startDate = col("start_date", Meta[ZonedDateTime])

  object projects extends TableDef("projects"):
    val id = col("id", Meta[String])
    val name = col("name", Meta[String])
    val description = col("description", Meta[String])
    val startDate = col("start_date", Meta[ZonedDateTime])
    val endDate = col("end_date", Meta[ZonedDateTime])
    val status = col("status", Meta[ProjectStatus])
    val budget = col("budget", Meta[BigDecimal], nullable = true)

  object employeeProject extends TableDef("employee_project"):
    val employeeId = col("employee_id", Meta[String])
    val projectId = col("project_id", Meta[String])

  val schema =
    schema"""
      type Query {
        company(id: String!): Company
        companies: [Company!]!
      }

      scalar DateTime

      type Company {
        id: String!
        name: String!
        industry: String!
        location: Location!
        foundedYear: Int!
        website: String
        email: String
        phone: String
        socialMedia: SocialMedia!
        employees: [Employee!]!
      }

      type Location {
        address: String!
        postCode: String!
        city: String!
        country: String!
      }

      type SocialMedia {
        facebook: String
        instagram: String
        twitter: String
        mastodon: String
        linkedIn: String
      }

      type Employee {
        firstName: String!
        lastName: String!
        email: String!
        phone: String
        position: String!
        department: String!
        startDate: DateTime!
        projects: [Project!]!
      }

      type Project {
        name: String!
        description: String!
        startDate: DateTime!
        endDate: DateTime!
        status: ProjectStatus!
        budget: Float
      }

      enum ProjectStatus {
        New
        InProgress
        Completed
        Cancelled
      }
    """

  val QueryType = schema.ref("Query")
  val CompanyType = schema.ref("Company")
  val LocationType = schema.ref("Location")
  val SocialMediaType = schema.ref("SocialMedia")
  val EmployeeType = schema.ref("Employee")
  val ProjectType = schema.ref("Project")

  val DateTimeType = schema.ref("DateTime")
  val ProjectStatusType = schema.ref("ProjectStatus")

  val typeMappings =
    List(
      ObjectMapping(
        tpe = QueryType,
        fieldMappings = List(
          SqlObject("company"),
          SqlObject("companies")
        )
      ),
      ObjectMapping(
        tpe = CompanyType,
        fieldMappings = List(
          SqlField("id", companies.id, key = true),
          SqlField("name", companies.name),
          SqlField("industry", companies.industry),
          SqlObject("location"),
          SqlField("foundedYear", companies.foundedYear),
          SqlField("website", companies.website),
          SqlField("email", companies.email),
          SqlField("phone", companies.phone),
          SqlObject("socialMedia"),
          SqlObject("employees", Join(companies.id, employees.companyId))
        )
      ),
      ObjectMapping(
        tpe = LocationType,
        fieldMappings = List(
          SqlField("id", companies.id, key = true, hidden = true),
          SqlField("address", companies.locationAddress),
          SqlField("postCode", companies.locationPostCode),
          SqlField("city", companies.locationCity),
          SqlField("country", companies.locationCountry)
        )
      ),
      ObjectMapping(
        tpe = SocialMediaType,
        fieldMappings = List(
          SqlField("id", companies.id, key = true, hidden = true),
          SqlField("facebook", companies.socialMediaFacebook),
          SqlField("instagram", companies.socialMediaInstagram),
          SqlField("twitter", companies.socialMediaTwitter),
          SqlField("mastodon", companies.socialMediaMastodon),
          SqlField("linkedIn", companies.socialMediaLinkedIn)
        )
      ),
      ObjectMapping(
        tpe = EmployeeType,
        fieldMappings = List(
          SqlField("id", employees.id, key = true, hidden = true),
          SqlField("firstName", employees.firstName),
          SqlField("lastName", employees.lastName),
          SqlField("email", employees.email),
          SqlField("phone", employees.phone),
          SqlField("position", employees.position),
          SqlField("department", employees.department),
          SqlField("startDate", employees.startDate),
          SqlObject("projects", Join(employees.id, employeeProject.employeeId), Join(employeeProject.projectId, projects.id))
        )
      ),
      ObjectMapping(
        tpe = ProjectType,
        fieldMappings = List(
          SqlField("id", projects.id, key = true, hidden = true),
          SqlField("name", projects.name),
          SqlField("description", projects.description),
          SqlField("startDate", projects.startDate),
          SqlField("endDate", projects.endDate),
          SqlField("status", projects.status),
          SqlField("budget", projects.budget)
        )
      ),
      LeafMapping[ZonedDateTime](DateTimeType),
      LeafMapping[ProjectStatus](ProjectStatusType)
    )
  override val selectElaborator: SelectElaborator = new SelectElaborator(
    Map(
      QueryType -> {
        case s @ Select("company", List(Binding("id", StringValue(id))), child) =>
          Select(s.name, Nil, Unique(Filter(Eql(UniquePath(List("id")), Const(id)), child))).success
      }
    )
  )

object CompaniesMapping extends LoggedDoobieMappingCompanion:
  def mkMapping[F[_]: Sync](transactor: Transactor[F], monitor: DoobieMonitor[F]): CompaniesMapping[F] =
    new DoobieMapping(transactor, monitor) with CompaniesMapping[F]

  def mkMappingFromTransactor[F[_]: Sync](transactor: Transactor[F]): Mapping[F] =
    implicit val logger: Logger[F] = Slf4jLogger.getLoggerFromName[F]("SqlQueryLogger")
    mkMapping(transactor)
