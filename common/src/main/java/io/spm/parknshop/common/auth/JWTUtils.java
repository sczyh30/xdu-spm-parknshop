package io.spm.parknshop.common.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Util class provides fundamental functions for JWT authentication.
 *
 * @author Eric Zhao
 */
public final class JWTUtils {

  private static final String SECRET = "$s9x[ask2.%(92*D&9odd03}lD3./sd6skcS)D{Px-cl,w0md3I(CJ";

  private static final String ISSUER = "sczyh30";

  public static Mono<String> generateToken(String username, Long id, int role) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET);
      String token = JWT.create()
        .withIssuer(ISSUER)
        .withExpiresAt(DateUtils.toDate(LocalDateTime.now().plusDays(7)))
        .withClaim("username", username)
        .withClaim("id", id)
        .withClaim("role", role)
        .withClaim("sd", new Date())
        .sign(algorithm);
      return Mono.just(token);
    } catch (Exception ex){
      return Mono.error(ex);
    }
  }

  public static Mono<DecodedJWT> verifyToken(String token) {
    if (StringUtils.isEmpty(token)) {
      return Mono.error(ExceptionUtils.invalidParam("token"));
    }
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET);
      DecodedJWT decodedJWT = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build()
        .verify(token);
      return Mono.just(decodedJWT);
    } catch (JWTVerificationException ex) {
      return Mono.error(ExceptionUtils.invalidToken());
    } catch (Exception ex) {
      return Mono.error(ex);
    }
  }

  private JWTUtils() {}
}
