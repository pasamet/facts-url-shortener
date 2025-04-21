package com.example.facts

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FactServiceTest {
    @MockK
    lateinit var factStorage: FactStorage

    @MockK
    lateinit var factClient: FactClient

    val factService by lazy { FactService(factStorage, factClient, "en") }

    @Test
    fun `When shortenNextRandomFact called Then stores the fact`() {
        coEvery { factClient.random(any()) } returns Fact(
            id = "id",
            text = "text",
            source = "source",
            sourceUrl = "sourceUrl",
            language = "language",
            permalink = "permalink",
        )
        every { factStorage.store(any()) } returns "key"

        runBlocking {
            val actual = factService.shortenNextRandomFact()

            assertEquals(
                ShortenedFact(
                    originalFact = "text",
                    shortenedUrl = "key"
                ), actual
            )

            coVerify { factClient.random("en") }
            verify {
                factStorage.store(
                    StoredFact(
                        fact = "text",
                        originalPermalink = "permalink"
                    )
                )
            }
        }
    }

    @Test
    fun `When getFacts called Then returns the stored facts`() {
        val facts = listOf(
            StoredFact(fact = "Fact 1", originalPermalink = "url1"),
            StoredFact(fact = "Fact 2", originalPermalink = "url2")
        )
        every { factStorage.retrieveAll() } returns facts

        val actual = factService.facts()

        assertEquals(facts, actual)
    }

    @Test
    fun `When getFact called Then returns the stored fact`() {
        val fact = StoredFact(fact = "Fact 1", originalPermalink = "url1")
        every { factStorage.retrieve("42") } returns fact

        val actual = factService.fact("42")

        assertEquals(fact, actual)
    }
}
