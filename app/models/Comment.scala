package models

import play.api.libs.json.Json

case class Comment(id: Long, postId: Long, text: String, authorName: String)

object Comment {
  implicit val format = Json.format[Comment]
}
