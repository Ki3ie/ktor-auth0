package com.ki3ie

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Principal

data class CustomPayload(val payload: Payload) : Principal