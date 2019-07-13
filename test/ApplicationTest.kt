package com.ki3ie

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/public").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            handleRequest(HttpMethod.Get, "/private").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            handleRequest(HttpMethod.Get, "/private-scoped").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}
