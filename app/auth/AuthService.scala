package auth

import javax.inject.Inject
import pdi.jwt.JwtClaim
import play.api.Configuration
import pdi.jwt.{JwtAlgorithm, JwtBase64, JwtClaim, JwtJson}

import scala.util.Try
import scala.util.matching.Regex

class AuthService @Inject()(config: Configuration) {

  private val jwtRegex: Regex = """(.+?)\.(.+?)\.(.+?)""".r

  private def domain = config.get[String]("auth0.domain")
  private def audience = config.get[String]("auth0.audience")
  private def issuer = s"https://$domain"

  def validateJwt(token: String): Try[JwtClaim] = for{
    jwk <- getJwk(token)
    claims <- JwtJson.decode(token, jwk.getPublicKey, Seq(JwtAlgorithm.RS256))
    _<- validateClaims(claims)
  } yield claims
}
