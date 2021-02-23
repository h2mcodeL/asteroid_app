package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class AsteroidStatus {LOADING, SUCCESS, FAILED}

    enum class MenuItemFilter(val value: String) {SHOW_WEEK("week"), SHOW_TODAY("today"), SAVED("saved")}

    /**as we are using the repository to get the data, we will use databse */

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    //this _status will be used for navigation
    private val _navigationToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid: LiveData<Asteroid?>
        get() = _navigationToAsteroid

    //menu setup
    private val _menuItem = MutableLiveData<MenuItemFilter>()
    private val menuItem: LiveData<MenuItemFilter>
        get() = _menuItem

    private val _status = MutableLiveData<AsteroidStatus>()
    val status: LiveData<AsteroidStatus>
        get() = _status


    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
            get() = _asteroidList


    //add the new asteroidList
    /** set up the requirements for the menu
     * create the asteroidList LiveData item for binding with the xml file along with its getter
     * then create an observer that observes the Asteroids within a list
     */

    private val asteroidListObserver = Observer<List<Asteroid>> {
        //updated new list to RecyclerView
        _asteroidList.value = it
    }

    private var asteroidListLiveData: LiveData<List<Asteroid>>

    init {
        //initialise the list of saved asteroids - new code
        asteroidListLiveData = asteroidRepository.getAsteroidSelection(MenuItemFilter.SAVED)
        asteroidListLiveData.observeForever(asteroidListObserver)

        viewModelScope.launch {
        refreshData()
    }
}

    val images = asteroidRepository.imageOfDay


    private fun refreshData() {     //(filter: MainFragment.MenuItemFilter) {
        viewModelScope.launch {
            asteroidRepository.refreshImageOfDay()
            asteroidRepository.refreshAsteroids()
        }
    }


    //menu filter
     fun updateFilter(filter: MenuItemFilter) {
        //new code
     asteroidListLiveData = asteroidRepository.getAsteroidSelection(filter)
        asteroidListLiveData.observeForever(asteroidListObserver)
    }

    //we need to clear the asteroids in the observer
    override fun onCleared() {
        super.onCleared()
        asteroidListLiveData.removeObserver(asteroidListObserver)
    }



    //  this is for the displayDetails
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigationToAsteroid.value = asteroid
    }

    //this is to clear the existing value
    fun displayDetailComplete() {
        _navigationToAsteroid.value = null
    }
}