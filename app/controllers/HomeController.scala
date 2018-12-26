package controllers

import auth.AuthAction
import javax.inject._
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import repositories.DataRepository

import scala.util.{Failure, Random, Success, Try}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               dataRepository: DataRepository,
                               authAction: AuthAction)
  extends AbstractController(cc) with I18nSupport {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request =>
    Ok(views.html.index())
  }

  def hello(name: String) = Action {
    Ok("hello! " + name)
  }

  def addUser() = Action { implicit request =>
    val body = request.body

    body.asFormUrlEncoded match {
      case Some(map) => Ok(s"The user name: ${map("name").head}, age ${map("age").head}")
      case None => BadRequest("Unknown body format")
    }

  }

  def sqrt(num: String) = Action { implicit req =>
    Try(num.toInt) match {
      case Success(ans) if ans >= 0 => Ok(s"The answer is: ${math.sqrt(ans)}")
        .withHeaders("Client" -> "Play!")
        .withCookies(Cookie("id", Random.nextInt().toString))
      case Success(ans) => BadRequest(s"The input ${ans} should be greater than zero!")
      case Failure(ex) => Results.InternalServerError
    }
  }

  def getPost(postId: Long) = authAction { implicit req =>
    println("i'm in secured endpoint!")
    Logger.info("Im in secured endpoint")
    dataRepository.getPost(postId) map { post =>
      Ok(Json.toJson(post))
    } getOrElse NotFound

  }

  def getComments(postId: Long) = Action { implicit req =>
    Ok(Json.toJson(dataRepository.getComments(postId)))

  }
}
