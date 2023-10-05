package com.example.applicationform.presentation.application.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.applicationform.R
import com.example.applicationform.databinding.FragmentApplicationBinding
import com.example.applicationform.presentation.application.models.HouseInputState
import com.example.applicationform.presentation.application.models.ScreenState
import com.example.applicationform.presentation.application.view_model.ApplicationViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class ApplicationFragment : Fragment() {
    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplicationViewModel by viewModel()
    private lateinit var streetAdapter: StreetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.streets.observe(viewLifecycleOwner) { streets ->
            streetAdapter = StreetAdapter(
                requireContext(), R.layout.text_item, streets
            ) { street ->
                Snackbar.make(
                    binding.applicationConstraintLayout,
                    street.streetId,
                    Snackbar.LENGTH_SHORT
                ).show()

                binding.autoStreetText.setText(street.streetName)
                binding.autoStreetText.setSelection(binding.autoStreetText.text.length)
                binding.autoStreetText.setAdapter(null)
                binding.autoStreetText.clearFocus()
                viewModel.getHousesOnStreet(street)
            }

            //binding.autoStreetText.setAdapter(streetAdapter)
        }

        viewModel.houses.observe(viewLifecycleOwner) { list ->
            val mappedList = list.map { it.houseName }
            binding.autoTextHouse.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.text_item,
                    mappedList
                )
            )
        }

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            manageContent(state)
        }

        viewModel.houseInputState.observe(viewLifecycleOwner){state->
            manageHouseInput(state)
        }

        viewModel.isButtonEnabled.observe(viewLifecycleOwner){isEnable->
            manageSendButtonVisibility(isEnable)
        }

        binding.autoStreetText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length >= 3){
                    binding.autoStreetText.setAdapter(streetAdapter)
                }

                viewModel.validateStreet(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                changeClearButtonVisibility(s)
            }
        })

        binding.autoTextHouse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateHouse(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.clearButton.setOnClickListener {
            binding.autoStreetText.setText("")
        }

        binding.sendApplicationButton.setOnClickListener {
            hideKeyboard()
            viewModel.sendApplication(requireContext())
        }

        addTextListenersForManualFields()

    }

    private fun manageContent(screenState: ScreenState) {
        with(binding) {
            when (screenState) {
                ScreenState.StreetAndHouseFromBase -> {
                    houseNumberDrop.visibility = View.VISIBLE
                    tlHouse.visibility = View.GONE
                    tlSector.visibility = View.GONE
                }

                ScreenState.StreetFromBase -> {
                    houseNumberDrop.visibility = View.VISIBLE
                    tlHouse.visibility = View.VISIBLE
                    tlSector.visibility = View.VISIBLE
                }

                ScreenState.StreetNotFromBase -> {
                    houseNumberDrop.visibility = View.GONE
                    tlHouse.visibility = View.VISIBLE
                    tlSector.visibility = View.VISIBLE
                }

            }
        }
    }


    private fun manageHouseInput(houseInputState: HouseInputState) {
        when (houseInputState) {
            HouseInputState.Default -> {}
            HouseInputState.IncorrectInput -> {
                hideKeyboard()
                Snackbar.make(
                    binding.applicationConstraintLayout,
                    R.string.wrong_input_house,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setActionTextColor(resources.getColor(R.color.white, null))
                    .setAction("OK") {}
                    .show()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.applicationConstraintLayout.windowToken, 0)
    }

    private fun changeClearButtonVisibility(input: Editable?){
        binding.clearButton.visibility = if (input.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun addTextListenersForManualFields(){

        binding.etHouse.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getHouseInput(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etSector.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getSectorInput(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etFlat.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.getFlatInput(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun manageSendButtonVisibility(isEnable: Boolean){
        binding.sendApplicationButton.isEnabled = isEnable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}