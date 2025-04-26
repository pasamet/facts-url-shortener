package com.example.facts

import io.quarkus.logging.Log
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.WebApplicationException
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI

@Path("/facts")
class FactResource(private val service: FactService) {

    @POST
    @Operation(
        summary = "Creates a short link",
        description = "Stores a short link to a fact entry and returns the fact text and the created short link"
    )
    @APIResponse(
        name = "Fact text and the short link"
    )
    suspend fun shortenFact(): ShortenedFact {
        try {
            return service.shortenNextRandomFact()
        } catch (e: Exception) {
            Log.error("shortenFact failed", e)
            throw WebApplicationException()
        }
    }

    @GET
    @Operation(
        summary = "Lists stored facts",
    )
    @APIResponse(
        responseCode = "200",
        name = "List of stored facts and their permanent links"
    )
    fun getFacts(): Iterable<StoredFact> = service.facts()

    @GET
    @Path("/{shortenedUrl}")
    @Operation(
        summary = "Fact for the given short link ",
    )
    @APIResponse(
        responseCode = "200",
        name = "Stored fact text and its permanent link"
    )
    @APIResponse(
        responseCode = "404",
        description = "Short link not found"
    )
    fun getFact(@RestPath shortenedUrl: String): StoredFact {
        val fact = service.fact(shortenedUrl)
        return fact ?: throw WebApplicationException(404)
    }

    @GET
    @Path("/{shortenedUrl}/redirect")
    @Operation(
        summary = "Redirects to permanent link",
    )
    @APIResponse(
        responseCode = "303",
    )
    @APIResponse(
        responseCode = "404",
        description = "Short link not found"
    )
    fun redirectFact(@RestPath shortenedUrl: String): RestResponse<Unit> {
        val fact = service.fact(shortenedUrl)

        return fact?.run { RestResponse.seeOther(URI(originalPermalink)) }
            ?: throw WebApplicationException(404)
    }
}




