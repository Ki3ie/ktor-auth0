package com.ki3ie

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.UnauthorizedResponse
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.pipeline.PipelineInterceptor

enum class CustomScope(val permission: String) {
    READ_RULES("read:rules")
}

suspend fun PipelineContext<Unit, ApplicationCall>.scope(
    scope: CustomScope,
    body: PipelineInterceptor<Unit, ApplicationCall>
) {
    val principal = call.principal<JWTPrincipal>()
    val scopesForCurrentToken = principal?.payload?.claims?.get("scope")?.asString()?.split(" ") ?: emptyList()
    if (scopesForCurrentToken.contains(scope.permission)) {
        body(this.subject)
    } else {
        call.respond(
            UnauthorizedResponse(
                HttpAuthHeader.Parameterized(
                    "Bearer",
                    mapOf(HttpAuthHeader.Parameters.Realm to "Ktor auth0")
                )
            )
        )
    }
}