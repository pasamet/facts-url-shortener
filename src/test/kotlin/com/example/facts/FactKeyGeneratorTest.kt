package com.example.facts

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FactKeyGeneratorTest {
    private val factKeyGenerator = FactKeyGenerator()

    @Test
    fun `When nextKey call multiple times Then returns unique values in radix 36`() {
        val values = List(43) { factKeyGenerator.nextKey() }

        assertEquals(43, values.toSet().size)
        assertEquals("a", values[10])
    }
}
