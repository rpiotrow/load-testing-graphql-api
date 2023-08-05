package io.github.rpiotrow.graphql

import org.scalacheck.Gen
import org.scalacheck.Gen.*

import io.github.rpiotrow.graphql.Queries.CompaniesQuery
import io.github.rpiotrow.graphql.Queries.CompaniesQuery.*
import io.github.rpiotrow.graphql.Queries.CompanyQuery
import io.github.rpiotrow.graphql.Queries.Field

object QueriesGens:
  def companiesQuery: Gen[CompaniesQuery] =
    for
      fields <- companyFields
      itemsPerPage <- Gen.oneOf(ItemsPerPage.ItemsPerPage_5, ItemsPerPage.ItemsPerPage_10)
      //assuming there is at least 500 companies to query
      pageNumber <- Gen.chooseNum(1, 500 / itemsPerPage.value)
      orderBy <- Gen.oneOf(OrderBy.values.toSeq)
    yield CompaniesQuery(pageNumber, itemsPerPage, orderBy, fields)

  def companyQuery(id: String): Gen[CompanyQuery] =
    for
      fields <- companyFields
    yield CompanyQuery(id, fields)

  private def companyFields: Gen[Seq[CompanyField]] =
    flattenSequence(
      Seq(
        option(CompanyField.IdF),
        option(CompanyField.NameF),
        option(CompanyField.IndustryF),
        location,
        option(CompanyField.FoundedYearF),
        option(CompanyField.WebsiteF),
        option(CompanyField.EmailF),
        option(CompanyField.PhoneF),
        socialMedia,
        employees
      )
    )

  private def location: Gen[Option[CompanyField.LocationF]] =
    option {
      flattenSequence(
        Seq(
          option(LocationField.AddressF),
          option(LocationField.PostCodeF),
          option(LocationField.CityF),
          option(LocationField.CountryF)
        )
      ).map(CompanyField.LocationF.apply)
    }

  private def socialMedia: Gen[Option[CompanyField.SocialMediaF]] =
    option {
      flattenSequence(
        Seq(
          option(SocialMediaField.FacebookF),
          option(SocialMediaField.InstagramF),
          option(SocialMediaField.TwitterF),
          option(SocialMediaField.MastodonF),
          option(SocialMediaField.LinkedInF)
        )
      ).map(CompanyField.SocialMediaF.apply)
    }

  private def employees: Gen[Option[CompanyField.EmployeeF]] =
    option {
      flattenSequence(
        Seq(
          option(EmployeeField.FirstNameF),
          option(EmployeeField.LastNameF),
          option(EmployeeField.EmailF),
          option(EmployeeField.PhoneF),
          option(EmployeeField.PositionF),
          option(EmployeeField.DepartmentF),
          option(EmployeeField.StartDateF),
          projects
        )
      ).map(CompanyField.EmployeeF.apply)
    }

  private def projects: Gen[Option[EmployeeField.ProjectsF]] =
    option {
      flattenSequence(
        Seq(
          option(ProjectField.NameF),
          option(ProjectField.DescriptionF),
          option(ProjectField.StartDateF),
          option(ProjectField.EndDateF),
          option(ProjectField.StatusF),
          option(ProjectField.BudgetF)
        )
      ).map(EmployeeField.ProjectsF.apply)
    }

  private def flattenSequence[A <: Field](gens: Seq[Gen[Option[A]]]): Gen[Seq[A]] =
    Gen.sequence[Seq[Option[A]], Option[A]](gens).map(_.flatten)
