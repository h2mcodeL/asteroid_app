package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

    class MainFragment : Fragment() {

        //need access to the menu for selection
        //enum class MenuItemFilter(val value: String) { SHOW_WEEK("showweek"), SHOW_TODAY("today"), SAVED("saved") }

       // val activity = requireNotNull(this.activity){}

        private val viewModel: MainViewModel by lazy {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View {
            val binding = FragmentMainBinding.inflate(inflater)


            //binding observes livedata updates
            binding.lifecycleOwner = this

            //our layout knows about the viewmodel
            binding.viewModel = viewModel

            //we use the adapter to get the ListAdapter

            binding.asteroidRecycler.adapter = MainViewAdapter(MainViewAdapter.OnClickListener {
                viewModel.displayAsteroidDetails(it)
            })

            //navigate to selected property
            viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
                if (null != it) {
                    this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))           /*actionShowDetail(asteroid))*/
                    viewModel.displayDetailComplete()
                }
            })

            setHasOptionsMenu(true)

            return binding.root
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.main_overflow_menu, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }


        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            viewModel.updateFilter(
            when(item.itemId) {
                R.id.show_today_menu -> MainViewModel.MenuItemFilter.SHOW_TODAY
                R.id.show_week_menu -> MainViewModel.MenuItemFilter.SHOW_WEEK
                else -> MainViewModel.MenuItemFilter.SAVED
            })
            return true
        }
    }
