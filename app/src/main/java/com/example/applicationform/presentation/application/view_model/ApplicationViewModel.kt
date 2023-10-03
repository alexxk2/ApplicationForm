package com.example.applicationform.presentation.application.view_model

import androidx.lifecycle.ViewModel
import com.example.applicationform.domain.application.GetAllHousesUseCase
import com.example.applicationform.domain.application.GetAllStreetsUseCase

class ApplicationViewModel(
    private val getAllHousesUseCase: GetAllHousesUseCase,
    private val getAllStreetsUseCase: GetAllStreetsUseCase
): ViewModel() {
}