package com.github.deen13

import io.vertx.core.json.JsonObject

data class LoginRequest(val username: String, val password: String) {

  fun matches(credentials: JsonObject): Boolean {
    return this.username == credentials.getString("username") && this.password == credentials.getString("password")
  }
}
