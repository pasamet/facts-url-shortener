package com.example.facts

import jakarta.ws.rs.container.ContainerRequestContext
import org.jboss.resteasy.reactive.server.ServerRequestFilter

class AccessStatsFilter(private val storage: AccessStatsStorage) {
    @ServerRequestFilter
    fun recordAccess(context: ContainerRequestContext) {
        storage.record(context.method, context.uriInfo)
    }

}

