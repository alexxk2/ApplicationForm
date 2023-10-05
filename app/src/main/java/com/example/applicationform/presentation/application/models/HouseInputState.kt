package com.example.applicationform.presentation.application.models

sealed interface HouseInputState{
    object Default: HouseInputState
    object IncorrectInput: HouseInputState
}