package com.ki3ie

import com.auth0.jwk.UrlJwkProvider
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(Authentication) {
        jwt {
            // Skip Authentication when deployment environment is dev
            skipWhen { ConfigFactory.load().getString("ktor.deployment.environment") == "dev" }
            verifier(UrlJwkProvider(ConfigFactory.load().getString("auth0.issuer")))
            validate { credential ->
                val payload = credential.payload
                if (payload.audience.contains(ConfigFactory.load().getString("auth0.audience"))) {
                    CustomPayload(payload)
                } else {
                    null
                }
            }
        }
    }

    install(Routing) {
        get("/public") {
            call.respond("Hello from a public endpoint! You don't need to be authenticated to see this.")
        }

        authenticate {
            get("/private") {
                call.respond("Hello from a private endpoint! You need to be authenticated to see this.")
            }
        }
    }
}