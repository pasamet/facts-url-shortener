package com.example.facts

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@QuarkusTest
class AdminResourceIntegrationTest {

    @Test
    fun `Given one fact accessed 3 times When stats requested Then response access count is 3`() {
        When {
            get("/facts/1")
            get("/facts/1")
            get("/facts/1/redirect")

            get("/admin/statistics")
        } Then {
            statusCode(200)
            body("[0].access_count", Matchers.equalTo(3))
            body("[0].shortened_url", Matchers.equalTo("1"))
        }
    }
}
