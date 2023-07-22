package io.github.rpiotrow.graphql

import io.github.rpiotrow.graphql.Queries.CompaniesQuery.CompanyField
import io.github.rpiotrow.graphql.Queries.CompaniesQuery.{ItemsPerPage, OrderBy}

object Queries:
  sealed trait Query:
    def fields: Seq[Field]

  case class CompaniesQuery(
    pageNumber: Int = 1,
    itemsPerPage: ItemsPerPage = ItemsPerPage.ItemsPerPage_5,
    orderBy: OrderBy = OrderBy.OrderByNameAscending,
    fields: Seq[CompanyField]
  ) extends Query

  case class CompanyQuery(id: String, fields: Seq[CompanyField]) extends Query

  object CompaniesQuery:
    enum CompanyField(val name: String) extends Field:
      case IdF extends CompanyField("id") with Leaf
      case NameF extends CompanyField("name") with Leaf
      case IndustryF extends CompanyField("industry") with Leaf
      case LocationF(val fields: Seq[LocationField]) extends CompanyField("location") with NonLeaf
      case FoundedYearF extends CompanyField("foundedYear") with Leaf
      case WebsiteF extends CompanyField("website") with Leaf
      case EmailF extends CompanyField("email") with Leaf
      case PhoneF extends CompanyField("phone") with Leaf
      case SocialMediaF(val fields: Seq[SocialMediaField]) extends CompanyField("socialMedia") with NonLeaf
      case EmployeeF(val fields: Seq[EmployeeField]) extends CompanyField("employees") with NonLeaf
    enum LocationField(val name: String) extends Field:
      case AddressF extends LocationField("address") with Leaf
      case PostCodeF extends LocationField("postCode") with Leaf
      case CityF extends LocationField("city") with Leaf
      case CountryF extends LocationField("country") with Leaf
    enum SocialMediaField(val name: String) extends Field:
      case FacebookF extends SocialMediaField("facebook") with Leaf
      case InstagramF extends SocialMediaField("instagram") with Leaf
      case TwitterF extends SocialMediaField("twitter") with Leaf
      case MastodonF extends SocialMediaField("mastodon") with Leaf
      case LinkedInF extends SocialMediaField("linkedIn") with Leaf
    enum EmployeeField(val name: String) extends Field:
      case FirstNameF extends EmployeeField("firstName") with Leaf
      case LastNameF extends EmployeeField("lastName") with Leaf
      case EmailF extends EmployeeField("email") with Leaf
      case PhoneF extends EmployeeField("phone") with Leaf
      case PositionF extends EmployeeField("position") with Leaf
      case DepartmentF extends EmployeeField("department") with Leaf
      case StartDateF extends EmployeeField("startDate") with Leaf
      case ProjectsF(fields: Seq[ProjectField]) extends EmployeeField("projects") with NonLeaf
    enum ProjectField(val name: String) extends Field:
      case NameF extends ProjectField("name") with Leaf
      case DescriptionF extends ProjectField("description") with Leaf
      case StartDateF extends ProjectField("startDate") with Leaf
      case EndDateF extends ProjectField("endDate") with Leaf
      case StatusF extends ProjectField("status") with Leaf
      case BudgetF extends ProjectField("budget") with Leaf

    enum ItemsPerPage(val value: Int):
      case ItemsPerPage_5 extends ItemsPerPage(5)
      case ItemsPerPage_10 extends ItemsPerPage(10)
      case ItemsPerPage_20 extends ItemsPerPage(20)
      case ItemsPerPage_30 extends ItemsPerPage(30)
      case ItemsPerPage_50 extends ItemsPerPage(50)
      case ItemsPerPage_75 extends ItemsPerPage(75)
      case ItemsPerPage_100 extends ItemsPerPage(100)

    enum OrderBy:
      case OrderByNameAscending
      case OrderByNameDescending
      case OrderByIndustryAscending
      case OrderByIndustryDescending
      case OrderByFoundedYearAscending
      case OrderByFoundedYearDescending

  sealed trait Field:
    def name: String

    def fold[A](onLeaf: => A, onNonLeaf: Seq[Field] => A): A = this match
      case s: Leaf    => onLeaf
      case s: NonLeaf => onNonLeaf(s.fields)

  sealed trait Leaf extends Field

  sealed trait NonLeaf extends Field:
    def fields: Seq[Field]
