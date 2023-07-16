package io.github.rpiotrow.domain

import java.util.UUID
import java.time.Instant

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
