package com.example.applicationform.presentation.application.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.applicationform.R
import com.example.applicationform.databinding.FragmentApplicationBinding
import com.example.applicationform.domain.models.Street
import com.example.applicationform.presentation.application.models.ScreenState
import com.example.applicationform.presentation.application.view_model.ApplicationViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
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

            binding.autoStreetText.setAdapter(streetAdapter)
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

        viewModel.screenState.observe(viewLifecycleOwner){state->
            manageContent(state)
        }


        binding.autoStreetText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.autoStreetText.setAdapter(streetAdapter)
                viewModel.validateStreet(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun manageContent(screenState: ScreenState){
        with(binding){
        when(screenState){
            ScreenState.StreetAndHouseFromBase -> {}
            ScreenState.StreetFromBase -> {
                houseNumberDrop.visibility = View.VISIBLE
            }
            ScreenState.StreetNotFromBase -> {
                houseNumberDrop.visibility = View.GONE
            }

        }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}