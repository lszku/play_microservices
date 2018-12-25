package auth

import javax.inject.Inject
import pdi.jwt.JwtClaim
import play.api.http.HeaderNames
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class UserRequest[A](jwtClaim: JwtClaim, token: String, request: Request[A])
  extends WrappedRequest[A](request)

class AuthAction @Inject()(bodyParser: BodyParsers.Default,
                           authService: AuthService)
                          (implicit ec: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] {

  private val headerTokenRegex = """Bearer (.+?)""".r

  override def parser: BodyParser[AnyContent] = bodyParser

  override def invokeBlock[A](request: Request[A],
                              block: UserRequest[A] => Future[Result]): Future[Result] = {
    extractBearerToken(request) map { token =>
      authService.validateJwt(token) match {
        case Success(claim) => block(UserRequest(claim, token, request))
        case Failure(exception) => Future.successful(Results.Unauthorized(exception.getMessage))
      }
    } getOrElse Future.successful(Results.Unauthorized)
  }

  override protected def executionContext: ExecutionContext = ec

  private def extractBearerToken[A](request: Request[A]) =
    request.headers.get(HeaderNames.AUTHORIZATION).collect {
      case headerTokenRegex(token) => token
    }
}