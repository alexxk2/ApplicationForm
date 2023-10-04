package com.example.applicationform.presentation.application.models

sealed interface ScreenState{
    object StreetNotFromBase: ScreenState
    object StreetFromBase: ScreenState
    object StreetAndHouseFromBase: ScreenState
}

