package com.christhperalta.activosfijos.feature.login.data.remote.dto.loginDto

import com.christhperalta.activosfijos.feature.login.data.repository.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginDtoMapperTest {

    @Test
    fun toDomain_maps_all_fields_correctly() {
        val dto = LoginDto(
            odataMetadata = "http://schema",
            sessionId = "abc-123",
            sessionTimeout = 30,
            version = "2.0"
        )
        val domain = dto.toDomain()
        assertEquals("http://schema", domain.odataMetadata)
        assertEquals("abc-123", domain.sessionId)
        assertEquals(30, domain.sessionTimeout)
        assertEquals("2.0", domain.version)
    }

    @Test
    fun toDomain_handles_nullable_fields() {
        val dto = LoginDto(
            odataMetadata = "http://schema",
            sessionId = "",
            sessionTimeout = 0,
            version = ""
        )
        val domain = dto.toDomain()
        assertEquals("", domain.sessionId)
        assertEquals(0, domain.sessionTimeout)
        assertEquals("", domain.version)
        assertEquals("http://schema", domain.odataMetadata)
    }

    @Test
    fun toDomain_maps_empty_sessionId() {
        val dto = LoginDto(
            odataMetadata = "meta",
            sessionId = "",
            sessionTimeout = 10,
            version = "1.0"
        )
        val domain = dto.toDomain()
        assertEquals("", domain.sessionId)
        assertEquals(10, domain.sessionTimeout)
    }

    @Test
    fun toDomain_maps_zero_sessionTimeout() {
        val dto = LoginDto(
            odataMetadata = "meta",
            sessionId = "sess1",
            sessionTimeout = 0,
            version = "1.0"
        )
        val domain = dto.toDomain()
        assertEquals(0, domain.sessionTimeout)
    }
}
