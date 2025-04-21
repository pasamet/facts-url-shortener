package com.example.facts

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class FactStorage(private val generator: FactKeyGenerator) {
    private val map = ConcurrentHashMap<String, StoredFact>()
    private val permalinkToToKey = ConcurrentHashMap<String, String>()

    fun store(value: StoredFact): String {
        val permalink = value.originalPermalink
        val key = permalinkToToKey.computeIfAbsent(permalink) {
            generator.nextKey()
        }
        map[key] = value
        return key
    }

    fun retrieve(key: String): StoredFact? = map[key]

    open fun retrieveAll(): Iterable<StoredFact> = map.values
    
}

data class StoredFact(
    val fact: String,
    @field:JsonProperty("original_permalink")
    val originalPermalink: String,
)
