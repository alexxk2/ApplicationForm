package com.example.applicationform.presentation.application.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationform.domain.application.GetAllHousesUseCase
import com.example.applicationform.domain.application.GetAllStreetsUseCase
import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street
import com.example.applicationform.presentation.application.models.HouseInputState
import com.example.applicationform.presentation.application.models.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ApplicationViewModel(
    private val getAllHousesUseCase: GetAllHousesUseCase,
    private val getAllStreetsUseCase: GetAllStreetsUseCase
) : ViewModel() {


    private lateinit var allStreets: List<Street>

    private val _streets = MutableLiveData<List<Street>>()
    val streets: LiveData<List<Street>> = _streets

    private val _houses = MutableLiveData<List<House>>()
    val houses: LiveData<List<House>> = _houses

    private val _isAllFieldsFilled = MutableLiveData<Boolean>()
    val isAllFieldsFilled: LiveData<Boolean> = _isAllFieldsFilled

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.StreetNotFromBase)
    val screenState: LiveData<ScreenState> = _screenState

    private val _houseInputState = MutableLiveData<HouseInputState>(HouseInputState.Default)
    val houseInputState: LiveData<HouseInputState> = _houseInputState

    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private var currentStreet: Street? = null
    private var currentHouse: House? = null

    private var streetManualInput = ""
    private var houseAutoCompleteInput = ""
    private var houseManualInput = ""
    private var sectorManualInput = ""
    private var flatManualInput = ""

    init {
        getAllStreets()
    }

    private fun getAllStreets() {

        viewModelScope.launch(Dispatchers.IO) {
            _streets.postValue(getAllStreetsUseCase.execute())
        }
    }

    fun getHousesOnStreet(street: Street) {
        _screenState.postValue(ScreenState.StreetFromBase)

        viewModelScope.launch(Dispatchers.IO) {
            _houses.postValue(getAllHousesUseCase.execute(street.streetId))
            currentStreet = street
        }
    }

    fun validateStreet(streetName: String) {

        streetManualInput = streetName
        val allStreetsNames = _streets.value?.map { it.streetName }

        if (streetName == currentStreet?.streetName) {
            _screenState.value = ScreenState.StreetFromBase

        } else if (allStreetsNames?.contains(streetName) == true) {
            _screenState.value = ScreenState.StreetFromBase

            val newStreet = _streets.value?.firstOrNull { it.streetName == streetName }

            newStreet?.let {
                currentStreet = newStreet
                viewModelScope.launch(Dispatchers.IO) { getHousesOnStreet(newStreet) }
            }
        } else {
            _screenState.value = ScreenState.StreetNotFromBase
            currentStreet = null
        }

        manageSendButtonVisibility()
    }

    fun validateHouse(houseName: String) {

        houseAutoCompleteInput = houseName

        val housesNames = _houses.value?.map { it.houseName }

        if (housesNames?.contains(houseName) == true) {
            val newHouse = _houses.value?.firstOrNull { it.houseName == houseName }
            newHouse?.let {
                currentHouse = newHouse
                _screenState.value = ScreenState.StreetAndHouseFromBase
                _houseInputState.value = HouseInputState.Default
            }
        } else {
            currentHouse = null
            _screenState.value = ScreenState.StreetFromBase
            _houseInputState.value = HouseInputState.IncorrectInput

            viewModelScope.launch {
                delay(BACK_TO_DEFAULT_INPUT_STATE_DELAY)
                _houseInputState.postValue(HouseInputState.Default)
            }
        }
        manageSendButtonVisibility()
    }

    private fun manageSendButtonVisibility() {
        when (_screenState.value) {

            ScreenState.StreetAndHouseFromBase -> {
                _isButtonEnabled.value = flatManualInput.isNotBlank()
            }

            ScreenState.StreetFromBase -> {
                _isButtonEnabled.value =
                    streetManualInput.isNotBlank() && houseAutoCompleteInput.isNotBlank() && houseManualInput.isNotBlank() && sectorManualInput.isNotBlank() && flatManualInput.isNotBlank()
            }

            ScreenState.StreetNotFromBase -> {
                _isButtonEnabled.value =
                    streetManualInput.isNotBlank() && houseManualInput.isNotBlank() && sectorManualInput.isNotBlank() && flatManualInput.isNotBlank()
            }

            null -> {
                _isButtonEnabled.value = false
            }
        }


    }

    fun getHouseInput(input: String) {
        houseManualInput = input
        manageSendButtonVisibility()
    }

    fun getSectorInput(input: String) {
        sectorManualInput = input
        manageSendButtonVisibility()
    }

    fun getFlatInput(input: String) {
        flatManualInput = input
        manageSendButtonVisibility()
    }


    fun sendApplication(context: Context) {
        when (_screenState.value) {

            ScreenState.StreetAndHouseFromBase -> {
                val data =
                    "${currentStreet?.streetId} - 1, ${currentHouse?.houseId} - 2, $flatManualInput - 3"
                Toast.makeText(context, data, Toast.LENGTH_LONG).show()
            }

            ScreenState.StreetFromBase -> {
                val data =
                    "${currentStreet?.streetId} - 1, $houseManualInput -2, $sectorManualInput - 3, $flatManualInput - 4"
                Toast.makeText(context, data, Toast.LENGTH_LONG).show()
            }

            ScreenState.StreetNotFromBase -> {
                val data =
                    "$streetManualInput - 1, $houseManualInput -2, $sectorManualInput - 3, $flatManualInput - 4"
                Toast.makeText(context, data, Toast.LENGTH_LONG).show()
            }

            null -> {}
        }
    }

    companion object {
        const val BACK_TO_DEFAULT_INPUT_STATE_DELAY = 200L
    }
}