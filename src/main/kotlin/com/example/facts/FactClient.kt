package com.example.facts

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.jboss.resteasy.reactive.RestQuery

@RegisterRestClient(configKey = "fact-api")
interface FactClient {
    @GET
    @Path("/random")
    suspend fun random(
        @RestQuery language: String
    ): Fact
}

data class Fact(
    val id: String,
    val text: String,
    val source: String,
    @field:JsonProperty("source_url")
    val sourceUrl: String,
    val language: String,
    val permalink: String,
)
