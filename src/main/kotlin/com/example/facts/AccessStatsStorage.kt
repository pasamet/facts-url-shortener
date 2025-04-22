package com.example.facts

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.UriInfo
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@ApplicationScoped
class AccessStatsStorage {
    private val map = ConcurrentHashMap<String, AtomicLong>()

    fun record(method: String, uri: UriInfo) {
        if (method != "GET") {
            return;
        }

        val shortenedUrl = uri.pathParameters["shortenedUrl"]?.firstOrNull()

        shortenedUrl?.run {
            if (isNotEmpty()) {
                val count = map.getOrPut(this) {
                    AtomicLong()
                }.incrementAndGet()
                Log.info("$this: $count")
            }
        }
    }

    fun accessCounts(): List<AccessCount> =
        map.entries.map { (key, value) ->
            AccessCount(key, value.get())
        }.toList()
}

data class AccessCount(
    @field:JsonProperty("shortened_url")
    val shortenedUrl: String,
    @field:JsonProperty("access_count")
    val accessCount: Long,
)
