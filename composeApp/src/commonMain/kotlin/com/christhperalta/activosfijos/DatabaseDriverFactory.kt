package com.christhperalta.activosfijos

import app.cash.sqldelight.db.SqlDriver
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}