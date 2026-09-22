package com.christhperalta.activosfijos.core.utils


fun formatPrice(value: Long): String {
    val cents = value % 100
    val integer = (value / 100).toString().reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
    val decimals = cents.toString().padStart(2, '0').take(2)
    return "RD$ $integer.$decimals"
}