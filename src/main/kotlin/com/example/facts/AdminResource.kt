package com.example.facts

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/admin")
class AdminResource(private val storage: AccessStatsStorage) {

    @GET
    @Path("/statistics")
    fun getStats(): List<AccessCount> = storage.accessCounts()
}




