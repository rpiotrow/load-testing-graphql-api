package io.github.rpiotrow.scenarios

import edu.gemini.grackle.QueryMinimizer
import java.net.URLEncoder

object ScenariosSetup:
  val fullQuery: String = encode("""
    {
      companies {
        name
        industry
        location { address postCode city country }
        foundedYear
        website
        email
        phone
        socialMedia { facebook instagram twitter mastodon linkedIn }
        employees {
          firstName
          lastName
          email
          phone
          position
          department
          startDate
          projects {
            name
            description
            startDate
            endDate
            status
            budget
          }
        }
      }
    }""")

  private def encode(query: String): String =
    val minimized = QueryMinimizer.minimizeText(query).fold(message => throw new RuntimeException(message), identity)
    "query=" + URLEncoder.encode(minimized, "UTF-8")
