package com.christhperalta.activosfijos.feature.config.domain.model

data class ServerConfigModel(
    val protocol: String,
    val host: String,
    val port: String,
    val databaseName: String
)