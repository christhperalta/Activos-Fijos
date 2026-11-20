package com.christhperalta.activosfijos.feature.home.presentation

sealed interface HomeEvent {
    data object Retry : HomeEvent
    data object SessionExpiredAccepted : HomeEvent
}
