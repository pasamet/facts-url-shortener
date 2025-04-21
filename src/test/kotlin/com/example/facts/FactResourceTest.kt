package com.example.facts

import io.mockk.coEvery
import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.io.IOException

@QuarkusTest
class FactResourceTest {

    @InjectMock
    lateinit var factService: FactService

    @Test
    fun `When POST request received Then returns shortened fact`() {
        coEvery { factService.shortenNextRandomFact() } returns ShortenedFact(
            originalFact = "Fact 1",
            shortenedUrl = "url1"
        )

        When {
            post("/facts")
        } Then {
            statusCode(200)
            body("original_fact", equalTo("Fact 1"))
            body("shortened_url", equalTo("url1"))
        }
    }

    @Test
    fun `When POST request received and IOException occurred Then returns status 500`() {
        coEvery { factService.shortenNextRandomFact() } throws IOException()

        When {
            post("/facts")
        } Then {
            statusCode(500)
        }
    }

    @Test
    fun `When GET request received Then returns stored facts`() {
        every { factService.facts() } returns listOf(
            StoredFact(fact = "Fact 1", originalPermalink = "url1"),
            StoredFact(fact = "Fact 2", originalPermalink = "url2")
        )

        When {
            get("/facts")
        } Then {
            statusCode(200)
            body("[0].fact", equalTo("Fact 1"))
            body("[1].fact", equalTo("Fact 2"))
            body("[0].original_permalink", equalTo("url1"))
            body("[1].original_permalink", equalTo("url2"))
        }
    }

    @Test
    fun `Given fact with key exists When GET fact request received Then returns the fact`() {
        every { factService.fact("key") } returns StoredFact(
            fact = "Fact 1",
            originalPermalink = "url1"
        )

        When {
            get("/facts/key")
        } Then {
            statusCode(200)
            body("fact", equalTo("Fact 1"))
            body("original_permalink", equalTo("url1"))
        }
    }

    @Test
    fun `Given fact with key does not exist When GET fact request received Then status is 404`() {
        every { factService.fact("key") } returns null

        When {
            get("/facts/key")
        } Then {
            statusCode(404)
        }
    }

    @Test
    fun `Given fact with key exists When GET fact redirect request received Then redirects to the original url`() {
        every { factService.fact("key") } returns StoredFact(
            fact = "Fact 1",
            originalPermalink = "http://example.com/1"
        )

        Given {
            redirects().follow(false)
        } When {
            get("/facts/key/redirect")
        } Then {
            statusCode(303)
            header("Location", "http://example.com/1")
        }
    }

    @Test
    fun `Given fact with key does not exist When GET fact redirect request received Then status is 404`() {
        every { factService.fact("key") } returns null

        Given {
            redirects().follow(false)
        } When {
            get("/facts/key/redirect")
        } Then {
            statusCode(404)
        }
    }
}
