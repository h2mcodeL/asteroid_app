package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.domain.Asteroid

class DetailViewModel(asteroid_property: Asteroid, app: Application): AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<Asteroid>()
    val selectedProperty: LiveData<Asteroid>
        get() = _selectedProperty

    init {
        _selectedProperty.value = asteroid_property
    }


}