package com.example.facts

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse

@Path("/admin")
class AdminResource(private val storage: AccessStatsStorage) {

    @GET
    @Path("/statistics")
    @Operation(
        summary = "List access counts",
    )
    @APIResponse(
        responseCode = "200",
        name = "List of short links and their access counts"
    )
    fun getStats(): List<AccessCount> = storage.accessCounts()
}




