package controllers

import javax.inject.{Inject, Singleton}
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc._
import authUtils.{User, auth}
import play.api.i18n.{I18nSupport, Langs}

@Singleton
class AuthController @Inject()(components: MessagesControllerComponents)
  extends MessagesAbstractController(components)
    with I18nSupport {

  val userForm: Form[User] = Form(
    mapping(
      "name" -> text,
      "password" -> text,
      "textArea" -> text

    )(User.apply)(User.unapply)
  )

  def index: Action[AnyContent] = Action { implicit req =>
    req.session.get("user") match {
      case Some(user) if auth.isValidUser(user) => Ok(s"Welcome $user")
      case Some(user) => BadRequest("Not a valid user!")
      case None => Redirect("/auth/login")
    }

  }

  def login = Action(parse.form(userForm)) { implicit req =>
    val userData = req.body
    val name = userData.name
    val password = userData.password
    if (auth.check(name, password))
      Redirect("/")
        .withSession(("user", name))
        .flashing("success"->s"User ${name} logged in")
    else
      BadRequest("Invalid username or password!")
  }

  def logout() = Action { implicit req =>
    Ok("Logged out!").withSession(("user", null))
  }

  def loginIndex = Action { implicit req =>
    Ok(views.html.login("Welcome, please login in!", userForm))


  }

}
