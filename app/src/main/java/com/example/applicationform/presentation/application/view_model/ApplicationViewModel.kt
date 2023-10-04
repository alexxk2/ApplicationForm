package com.example.applicationform.presentation.application.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationform.domain.application.GetAllHousesUseCase
import com.example.applicationform.domain.application.GetAllStreetsUseCase
import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street
import com.example.applicationform.presentation.application.models.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApplicationViewModel(
    private val getAllHousesUseCase: GetAllHousesUseCase,
    private val getAllStreetsUseCase: GetAllStreetsUseCase
): ViewModel() {


    private lateinit var allStreets: List<Street>

    private val _streets = MutableLiveData<List<Street>>()
    val streets: LiveData<List<Street>> = _streets

    private val _houses = MutableLiveData<List<House>>()
    val houses: LiveData<List<House>> = _houses

    private val _isAllFieldsFilled = MutableLiveData<Boolean>()
    val isAllFieldsFilled: LiveData<Boolean> = _isAllFieldsFilled

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.StreetNotFromBase)
    val screenState: LiveData<ScreenState> = _screenState

    private var currentStreet: Street = Street("-1","")

    init {
        getAllStreets()
    }

    private fun getAllStreets(){

        viewModelScope.launch(Dispatchers.IO) {
            _streets.postValue(getAllStreetsUseCase.execute())
        }
    }

    fun getHousesOnStreet(street: Street){
        _screenState.postValue(ScreenState.StreetFromBase)

        viewModelScope.launch(Dispatchers.IO) {
            _houses.postValue(getAllHousesUseCase.execute(street.streetId))
            currentStreet = street
        }
    }

    fun validateStreet(streetName: String){
        if (streetName != currentStreet.streetName){
            _screenState.value = ScreenState.StreetNotFromBase
        }
        else _screenState.value = ScreenState.StreetFromBase
    }



}