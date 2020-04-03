package com.github.deen13

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.deen13.Security.FRONTEND_USER_AUDIENCE
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.jwt.JWTOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.kotlin.core.json.jsonArrayOf
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.ext.auth.jwt.jwtAuthOptionsOf
import io.vertx.kotlin.ext.auth.secretOptionsOf
import java.time.Instant
import java.time.temporal.ChronoUnit

class MainVerticle : AbstractVerticle() {

  private val refreshTokenAuth by lazy {
    val secret = config().getJsonObject("authorization").getString("refresh-jwt-secret")
    val config = jwtAuthOptionsOf(secrets = listOf(secretOptionsOf(secret)))

    JWTAuth.create(vertx, config)
  }

  private val userTokenAuth by lazy {
    val secret = config().getJsonObject("authorization").getString("jwt-secret")

    val config = jwtAuthOptionsOf(
      secrets = listOf(secretOptionsOf(secret)),
      jwtOptions = JWTOptions()
        .setIssuer("main-verticle")
        .setSubject("user-interface")
        .setAudience(listOf(FRONTEND_USER_AUDIENCE))
    )

    JWTAuth.create(vertx, config)
  }

  private val mapper = jacksonObjectMapper()

  override fun start() {
    val router = Router.router(vertx)

    router.route("/api/*").handler(JWTAuthHandler.create(userTokenAuth, "/api/auth/"))
    router.route().handler(BodyHandler.create())

    router.post("/api/auth/login").handler(this::handleLogin)
    router.post("/api/auth/refresh").handler(this::handleTokenRefresh)

    router.get("/api/data/reviewers").handler(this::fetchReviewers)

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080)
  }

  private fun handleTokenRefresh(ctx: RoutingContext) {
    val tokenRefreshRequest = try {
      mapper.readValue<TokenRefreshRequest>(ctx.bodyAsString)
    } catch (e: JsonProcessingException) {
      ctx.response().setStatusCode(400).end()
      return
    }

    val authenticationOptions = jsonObjectOf("jwt" to tokenRefreshRequest.refreshToken)

    refreshTokenAuth.authenticate(authenticationOptions) {
      if (it.succeeded()) {
        ctx.response().end(generateUserToken().toBuffer())
      } else {
        ctx.response().setStatusCode(401).end()
      }
    }
  }

  private fun handleLogin(ctx: RoutingContext) {
    val loginRequest = try {
      mapper.readValue<LoginRequest>(ctx.bodyAsString)
    } catch (e: JsonProcessingException) {
      ctx.response().setStatusCode(400).end()
      return
    }

    if (loginRequest.matches(config().getJsonObject("authorization"))) {
      ctx.response().end(generateUserToken().toBuffer())
    } else {
      ctx.response().setStatusCode(401).end()
    }
  }

  private fun generateUserToken(): JsonObject {
    val accessTokenClaims = jsonObjectOf(
      "iss" to "main-verticle",
      "sub" to "user-interface",
      "exp" to Instant.now().plus(30L, ChronoUnit.SECONDS).epochSecond,
      "aud" to FRONTEND_USER_AUDIENCE
    )

    val refreshTokenClaims = jsonObjectOf(
      "sub" to "refreshing",
      "iss" to "main-verticle",
      "exp" to Instant.now().plus(90L, ChronoUnit.SECONDS).epochSecond,
      "aud" to FRONTEND_USER_AUDIENCE
    )

    return jsonObjectOf(
      "accessToken" to userTokenAuth.generateToken(accessTokenClaims),
      "refreshToken" to refreshTokenAuth.generateToken(refreshTokenClaims)
    )
  }

  private fun fetchReviewers(ctx: RoutingContext) {
    val reviewers = jsonArrayOf(
      "TrueCarry",
      "MathiasCiarlo",
      "JoaoPedroAS51",
      "MarcelloTheArcane",
      "leandropozas",
      "gaetansenn",
      "ruudboon"
    )

    ctx.response().end(reviewers.toBuffer())
  }
}
