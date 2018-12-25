package models

import play.api.libs.json.Json

case class Post(id: Long, content: String)

object Post{
  implicit val format = Json.format[Post]
}