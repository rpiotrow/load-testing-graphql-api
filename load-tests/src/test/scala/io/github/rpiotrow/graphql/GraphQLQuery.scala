package io.github.rpiotrow.graphql

import io.circe.{Encoder, Json}
import io.github.rpiotrow.graphql.Queries.*

import scala.util.control.NoStackTrace

case class GraphQLQueryError(message: String) extends NoStackTrace

case class GraphQLQuery(query: String) extends AnyVal

object GraphQLQuery:
  given Encoder[GraphQLQuery] = (q: GraphQLQuery) =>
    Json.obj(
      "query" -> Json.fromString(q.query)
    )

object GraphQLQueries:
  def from(query: Query): GraphQLQuery =
    GraphQLQuery(
      query match
        case companiesQuery: CompaniesQuery => build(companiesQuery)
        case companyQuery: CompanyQuery => build(companyQuery)
    )

  private def build(query: CompaniesQuery): String =
    s"""{ companies(pageNumber: ${query.pageNumber}, itemsPerPage: ${query.itemsPerPage}, orderBy: ${query.orderBy}) { ${build(query.fields)} } }"""

  private def build(query: CompanyQuery): String =
    s"""{ company(id: "${query.id}") { ${build(query.fields)} } }"""

  private def build(fields: Seq[Field]): String =
    fields.map {
      case field: Leaf => build(field)
      case obj: NonLeaf => build(obj)
    }.mkString

  private def build(field: Leaf): String = s""" ${field.name}"""
  private def build(obj: NonLeaf): String = if obj.isEmpty then "" else s""" ${obj.name} { ${build(obj.fields)} } """

  extension (nonLeaf: NonLeaf)
    private def isEmpty: Boolean =
      nonLeaf.fields.isEmpty || nonLeaf.fields.forall {
        case _: Leaf => false
        case nested: NonLeaf => nested.isEmpty
      }
