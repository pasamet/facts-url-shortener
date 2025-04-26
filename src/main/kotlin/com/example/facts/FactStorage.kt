package com.example.facts

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class FactStorage(private val generator: FactKeyGenerator) {
    private val map = ConcurrentHashMap<String, StoredFact>()
    private val permalinkToKey = ConcurrentHashMap<String, String>()

    /**
     * Stores given [value] and returns the storage key. If a value with same
     * `originalPermalink` is already stored, returns key of the previous value,
     * otherwise returns a new key.
     */
    fun store(value: StoredFact): String {
        val permalink = value.originalPermalink

        val key = permalinkToKey.computeIfAbsent(permalink) {
            generator.nextKey()
        }
        map[key] = value
        return key
    }

    fun retrieve(key: String): StoredFact? = map[key]

    fun retrieveAll(): List<StoredFact> = map.values.toList()

}

data class StoredFact(
    val fact: String,
    @field:JsonProperty("original_permalink")
    val originalPermalink: String,
)
