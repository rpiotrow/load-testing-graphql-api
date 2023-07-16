package io.github.rpiotrow.domain

case class SocialMedia(
  facebook: Option[String],
  instagram: Option[String],
  twitter: Option[String],
  mastodon: Option[String],
  linkedIn: Option[String]
)
