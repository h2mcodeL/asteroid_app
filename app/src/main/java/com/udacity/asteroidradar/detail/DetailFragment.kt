package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNSED_VARIABLE")
        val application = requireNotNull(activity).application      //this is needed for the Factory

        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this


        //how to resolve the issue with selectedAsteroid not working.
        //check dependencies?? this item selectedAsteroid is declared in the navGraph file xml. Models should be named models in lower case.
        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

      //  binding.


        //need to create the Factory
        val viewModelFactory = DetailViewModelFactory(asteroid, application)

        binding.detailViewmodel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())

            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
