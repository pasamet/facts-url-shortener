package com.example.facts

import io.quarkus.logging.Log
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.WebApplicationException
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI

@Path("/facts")
class FactResource(private val service: FactService) {

    @POST
    suspend fun shortenFact(): ShortenedFact {
        try {
            return service.shortenNextRandomFact()
        } catch (e: Exception) {
            Log.error("shortenFact failed", e)
            throw WebApplicationException()
        }
    }

    @GET
    fun getFacts(): Iterable<StoredFact> = service.facts()

    @GET
    @Path("/{shortenedUrl}")
    fun getFact(@RestPath shortenedUrl: String): StoredFact {
        val fact = service.fact(shortenedUrl)
        return fact ?: throw WebApplicationException(404)
    }

    @GET
    @Path("/{shortenedUrl}/redirect")
    fun redirectFact(@RestPath shortenedUrl: String): RestResponse<Unit> {
        val fact = service.fact(shortenedUrl)
        
        return fact?.run { RestResponse.seeOther(URI(originalPermalink)) }
            ?: throw WebApplicationException(404)
    }
}




