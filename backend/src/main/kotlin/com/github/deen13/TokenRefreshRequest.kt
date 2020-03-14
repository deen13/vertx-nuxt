package com.github.deen13

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenRefreshRequest(val refreshToken: String)
