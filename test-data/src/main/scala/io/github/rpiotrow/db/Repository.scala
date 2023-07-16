package io.github.rpiotrow.db

import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.*
import doobie.postgres.implicits.*
import io.github.rpiotrow.domain.*
import io.github.rpiotrow.db.Model.*

class Repository(transactor: Transactor[IO]):

  def insertCompany(company: Company): IO[Unit] =
    import company.*
    sql"""INSERT INTO companies(
         |  id,
         |  name,
         |  industry,
         |  location_address,
         |  location_post_code,
         |  location_city,
         |  location_country,
         |  founded_year,
         |  website,
         |  email,
         |  phone,
         |  social_media_facebook,
         |  social_media_instagram,
         |  social_media_twitter,
         |  social_media_mastodon,
         |  social_media_linked_in
         |)
         |VALUES (
         |  $id,
         |  $name,
         |  $industry,
         |  ${location.address},
         |  ${location.postCode},
         |  ${location.city},
         |  ${location.country},
         |  $foundedYear,
         |  $website,
         |  $email,
         |  $phone,
         |  ${socialMedia.facebook},
         |  ${socialMedia.instagram},
         |  ${socialMedia.twitter},
         |  ${socialMedia.mastodon},
         |  ${socialMedia.linkedIn}
         |)""".stripMargin.update.run.transact(transactor).void

  def insertEmployee(employee: Employee, company: Company): IO[Unit] =
    import employee.*
    sql"""INSERT INTO employees(
         |  id,
         |  company_id,
         |  first_name,
         |  last_Name,
         |  email,
         |  phone,
         |  position,
         |  department,
         |  start_date
         |)
         |VALUES (
         |  $id,
         |  ${company.id},
         |  $firstName,
         |  $lastName,
         |  $email,
         |  $phone,
         |  $position,
         |  $department,
         |  $startDate
         |)""".stripMargin.update.run.transact(transactor).void

  def insertProject(project: Project): IO[Unit] =
    import project.*
    sql"""INSERT INTO projects(
         |  id,
         |  name,
         |  description,
         |  start_date,
         |  end_date,
         |  status,
         |  budget
         |)
         |VALUES (
         |  $id,
         |  $name,
         |  $description,
         |  $startDate,
         |  $endDate,
         |  ${status.toString},
         |  $budget
         |)""".stripMargin.update.run.transact(transactor).void

  def insertEmployeeProject(employee: Employee, project: Project): IO[Unit] =
    sql"""INSERT INTO employee_project(
         |  employee_id,
         |  project_id
         |)
         |VALUES (
         |  ${employee.id},
         |  ${project.id}
         |)""".stripMargin.update.run.transact(transactor).void
