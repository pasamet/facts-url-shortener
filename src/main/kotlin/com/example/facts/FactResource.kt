package com.example.facts

import io.quarkus.logging.Log
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.WebApplicationException

@Path("/facts")
class FactResource(val service: FactService) {

    @POST
    suspend fun shortenFact(): ShortenedFact {
        try {
            return service.shortenNextRandomFact()
        } catch (e: Exception) {
            Log.error("shortenFact failed", e)
            throw WebApplicationException()
        }
    }
}




