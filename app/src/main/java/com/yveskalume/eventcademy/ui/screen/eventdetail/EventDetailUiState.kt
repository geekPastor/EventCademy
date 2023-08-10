package com.yveskalume.eventcademy.ui.screen.eventdetail

import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.entity.EventBooking

sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Success(val event: Event, val bookings: List<EventBooking>) : EventDetailUiState
    data class Error(val errorMessage: String) : EventDetailUiState
}