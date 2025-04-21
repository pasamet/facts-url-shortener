package com.example.facts

import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.atomic.AtomicLong

@ApplicationScoped
class FactKeyGenerator {
    private val keyCounter = AtomicLong()
    fun nextKey(): String = keyCounter.andIncrement.toString(36)
}
