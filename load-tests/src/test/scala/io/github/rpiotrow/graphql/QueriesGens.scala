package io.github.rpiotrow.graphql

import org.scalacheck.Gen
import org.scalacheck.Gen.*

import io.github.rpiotrow.graphql.Queries.CompaniesQuery
import io.github.rpiotrow.graphql.Queries.CompaniesQuery.*
import io.github.rpiotrow.graphql.Queries.CompanyQuery
import io.github.rpiotrow.graphql.Queries.Field

object QueriesGens:
  def companiesQueries: Gen[CompaniesQuery] =
    companyFields.map(CompaniesQuery.apply)

  def companyQueries: Gen[CompanyQuery] =
    for
      id <- Gen.uuid.map(_.toString)
      fields <- companyFields
    yield CompanyQuery(id, fields)

  private def companyFields =
    toFlatSequence(
      Seq(
        atMostOne(CompanyField.IdF),
        atMostOne(CompanyField.NameF),
        atMostOne(CompanyField.IndustryF),
        location,
        atMostOne(CompanyField.FoundedYearF),
        atMostOne(CompanyField.WebsiteF),
        atMostOne(CompanyField.EmailF),
        atMostOne(CompanyField.PhoneF),
        socialMedia,
        employees
      )
    )

  private def location: Gen[Option[CompanyField.LocationF]] =
    option {
      toFlatSequence(
        Seq(
          atMostOne(LocationField.AddressF),
          atMostOne(LocationField.PostCodeF),
          atMostOne(LocationField.CityF),
          atMostOne(LocationField.CountryF)
        )
      ).map(CompanyField.LocationF.apply)
    }

  private def socialMedia: Gen[Option[CompanyField.SocialMediaF]] =
    option {
      toFlatSequence(
        Seq(
          atMostOne(SocialMediaField.FacebookF),
          atMostOne(SocialMediaField.InstagramF),
          atMostOne(SocialMediaField.TwitterF),
          atMostOne(SocialMediaField.MastodonF),
          atMostOne(SocialMediaField.LinkedInF)
        )
      ).map(CompanyField.SocialMediaF.apply)
    }

  private def employees: Gen[Option[CompanyField.EmployeeF]] =
    option {
      toFlatSequence(
        Seq(
          atMostOne(EmployeeField.FirstNameF),
          atMostOne(EmployeeField.LastNameF),
          atMostOne(EmployeeField.EmailF),
          atMostOne(EmployeeField.PhoneF),
          atMostOne(EmployeeField.PositionF),
          atMostOne(EmployeeField.DepartmentF),
          atMostOne(EmployeeField.StartDateF),
          projects
        )
      ).map(CompanyField.EmployeeF.apply)
    }

  private def projects: Gen[Option[EmployeeField.ProjectsF]] =
    option {
      toFlatSequence(
        Seq(
          atMostOne(ProjectField.NameF),
          atMostOne(ProjectField.DescriptionF),
          atMostOne(ProjectField.StartDateF),
          atMostOne(ProjectField.EndDateF),
          atMostOne(ProjectField.StatusF),
          atMostOne(ProjectField.BudgetF)
        )
      ).map(EmployeeField.ProjectsF.apply)
    }

  private def atMostOne[T](gen: Gen[T]): Gen[Seq[T]] = oneOf(true, false).flatMap {
    case false => const(Seq.empty[T])
    case true  => gen.map(Seq(_))
  }

  private def toFlatSequence[A <: Field](gens: Iterable[Gen[IterableOnce[A]]]): Gen[Seq[A]] =
    Gen.sequence[Iterable[IterableOnce[A]], IterableOnce[A]](gens).map(_.flatten.toSeq)
