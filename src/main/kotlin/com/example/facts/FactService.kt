package com.example.facts

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class FactService(
    private val storage: FactStorage,
    @RestClient private val client: FactClient,
    @ConfigProperty(name = "fact.language", defaultValue = "en")
    private val language: String,
) {
    suspend fun shortenNextRandomFact(): ShortenedFact {
        val fact = client.random(language)
        val cached = StoredFact(
            fact = fact.text,
            originalPermalink = fact.permalink
        )
        val key = storage.store(cached)

        return ShortenedFact(
            originalFact = cached.fact,
            shortenedUrl = key
        )
    }
}

data class ShortenedFact(
    @field:JsonProperty("original_fact")
    val originalFact: String,
    @field:JsonProperty("shortened_url")
    val shortenedUrl: String,
)
