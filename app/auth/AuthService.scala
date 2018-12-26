package auth

import com.auth0.jwk.{Jwk, UrlJwkProvider}
import javax.inject.Inject
import pdi.jwt.JwtClaim
import play.api.Configuration
import pdi.jwt.{JwtAlgorithm, JwtBase64, JwtClaim, JwtJson}

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

class AuthService @Inject()(config: Configuration) {

  private val jwtRegex: Regex = """(.+?)\.(.+?)\.(.+?)""".r

  private def domain = config.get[String]("auth0.domain")

  private def audience = config.get[String]("auth0.audience")

  private def issuer = s"https://$domain"


  private val splitToken: String => Try[(String, String, String)] = (jwt: String) => jwt match {
    case jwtRegex(header, body, sig) => Success((header, body, sig))
    case _ => Failure(new Exception("Token does not match the correct pattern"))
  }

  private val decodeElements: Try[(String, String, String)] => Try[(String, String, String)] = (data: Try[(String, String, String)]) => data map {
    case (header, body, sig) =>
      (JwtBase64.decodeString(header), JwtBase64.decodeString(body), sig)
  }

  private val getJwk: String => Try[Jwk] = (token: String) => {
    (splitToken andThen decodeElements) (token) flatMap {
      case (header, _, _) => {
        val jwtHeader = JwtJson.parseHeader(header)
        val jwkProvider = new UrlJwkProvider(issuer)
        jwtHeader.keyId map { k =>
          Try(jwkProvider.get(k))
        } getOrElse Failure(new Exception("Unable to retrieve KID"))
      }
    }
  }

  def validateJwt(token: String): Try[JwtClaim] = for {
    jwk <- getJwk(token)
    claims <- JwtJson.decode(token, jwk.getPublicKey, Seq(JwtAlgorithm.RS256))
    _ <- validateClaims(claims)
  } yield claims

  private val validateClaims: JwtClaim => Try[JwtClaim] = (claims: JwtClaim) =>
    if (claims.isValid(issuer, audience)) {
      Success(claims)
    } else {
      Failure(new Exception("The JWT did not pass validation"))
    }
}
