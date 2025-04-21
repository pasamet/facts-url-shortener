package com.example.facts

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FactStorageTest {
    @MockK
    lateinit var factKeyGenerator: FactKeyGenerator

    @InjectMockKs
    lateinit var factStorage: FactStorage

    @BeforeEach
    fun setUp() {
        every { factKeyGenerator.nextKey() } returnsMany listOf(
            "a",
            "b",
            "c",
            "d"
        )
    }

    @Test
    fun `When store called Then returns the key`() {
        val actual = factStorage.store(
            StoredFact(
                fact = "fact",
                originalPermalink = "link",
            )
        )

        assertEquals("a", actual)
    }

    @Test
    fun `Given a fact is stored When store called with same link Then returns the previous key`() {
        val fact = StoredFact(
            fact = "fact1",
            originalPermalink = "link1",
        )
        factStorage.store(fact)

        val actual = factStorage.store(fact.copy(fact = "fact2"))

        assertEquals("a", actual)
        verify(exactly = 1) { factKeyGenerator.nextKey() }
    }

    @Test
    fun `Given a fact is stored When store called with different link Then returns the next key`() {
        val fact = StoredFact(
            fact = "fact1",
            originalPermalink = "link1",
        )
        factStorage.store(fact)

        val actual = factStorage.store(fact.copy(originalPermalink = "link2"))

        assertEquals("b", actual)
        verify(exactly = 2) { factKeyGenerator.nextKey() }
    }

    @Test
    fun `Given two facts are stored When retrieveAll called Then returns two facts`() {
        val fact1 = StoredFact(
            fact = "fact1",
            originalPermalink = "link1",
        )
        val fact2 = StoredFact(
            fact = "fact2",
            originalPermalink = "link2",
        )
        factStorage.store(fact1)
        factStorage.store(fact2)

        val actual = factStorage.retrieveAll()

        assertEquals(listOf(fact1, fact2), actual)
    }

    @Test
    fun `Given two facts are stored When retrieve called with first key Then returns the first fact`() {
        val fact1 = StoredFact(
            fact = "fact1",
            originalPermalink = "link1",
        )
        val fact2 = StoredFact(
            fact = "fact2",
            originalPermalink = "link2",
        )
        val key1 = factStorage.store(fact1)
        factStorage.store(fact2)

        val actual = factStorage.retrieve(key1)

        assertEquals(fact1, actual)
    }
}
