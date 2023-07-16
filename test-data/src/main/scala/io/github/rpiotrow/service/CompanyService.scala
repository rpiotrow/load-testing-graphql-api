package io.github.rpiotrow.service

import cats.effect.IO
import cats.implicits.given
import io.github.arainko.ducktape.*
import io.github.rpiotrow.db.Model.{Company as DbCompany, Employee as DbEmployee}
import io.github.rpiotrow.db.Repository
import io.github.rpiotrow.domain.*
import io.github.rpiotrow.mockaroo.Model.{Company as MockarooCompany, Employee as MockarooEmployee}
import io.github.rpiotrow.service.Model.*

import scala.util.Random

class CompanyService(repository: Repository):

  def storeCompanyEmployeesAndProjects(mockarooCompanies: List[MockarooCompany]): IO[Unit] =
    val companies = mockarooCompanies.map(toServiceModel)
    for
      // store companies
      _ <- companies.map(_.to[DbCompany]).traverse(repository.insertCompany)
      // store employees
      _ <- companies
        .map { company =>
          (company.to[DbCompany], company.employees.map(_.to[DbEmployee]))
        }
        .traverse { case (dbCompany, dbEmployees) =>
          dbEmployees.traverse(dbEmployee => repository.insertEmployee(dbEmployee, dbCompany))
        }
      // store projects
      _ <- mockarooCompanies.flatMap(_.projects).traverse(repository.insertProject)
      // store connections between employees and projects
      _ <- companies
        .flatMap(_.employees)
        .map { employee =>
          (employee.to[DbEmployee], employee.projects)
        }
        .traverse { case (dbEmployee, projects) =>
          projects.traverse(project => repository.insertEmployeeProject(dbEmployee, project))
        }
    yield ()

  private def toServiceModel(mockarooCompany: MockarooCompany): Company =
    mockarooCompany
      .into[Company]
      .transform(
        Field.computed(
          _.employees,
          _.employees.map(
            _.into[Employee].transform(
              Field.const(
                _.projects,
                Random.shuffle(mockarooCompany.projects).take(Random.nextInt(mockarooCompany.projects.length))
              )
            )
          )
        )
      )
