package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    //this section is used to load to the offline cache
    val imageOfDay: LiveData<PictureOfDay> = Transformations.map(database.asteroidDao.getImageOfDay()) {
        it?.asDomainModel()     //we add ?
        //it?.asDomainModel()
    }

    suspend fun refreshImageOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val imageOfTheDay = AsteroidApi.imageApiService.getImage(API_KEY)
                database.asteroidDao.insertPictureOfDay(imageOfTheDay.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("ASTEROID IMAGE", "Image has not downloaded: " + e.message)
            }
        }
    }


    fun getAsteroidSelection(filter: MainViewModel.MenuItemFilter): LiveData<List<Asteroid>> {
        return when (filter) {
                //option1
            (MainViewModel.MenuItemFilter.SAVED) -> {
                Transformations.map(database.asteroidDao.getAsteroids())
                {
                    it.asDomainModel()
                }
            }
            //option2
            (MainViewModel.MenuItemFilter.SHOW_TODAY) -> {
                Transformations.map(database.asteroidDao.getTodayAsteroid())
                {
                    it.asDomainModel()
                }
            }
            //option3
            else -> {
                Transformations.map(database.asteroidDao.getWeeklyAsteroid())
                {
                    it.asDomainModel()
                }
            }
        }
    }

    //transformations.map allows us to convert from one live data to another
    /** Refresh the asteroids in the offline cache.
     * This function uses the IO dispatcher to ensure the insert database operation works
     */

    suspend fun refreshAsteroids() {        //suspend means coroutine friendly.
        withContext(Dispatchers.IO) {       //we are reading from the database, using any dispatcher
            //this is used to make a network request within IO
            try {

                MainViewModel.AsteroidStatus.LOADING        //check the loading status
                //get asteroids using the network activity, make seven days public.
                val sevenDays = getNextSevenDaysFormattedDates()   //call this method from the network activity, [start pos], [end pos]
                val sevenDaysResult = AsteroidApi.asteroidApiService.getResults(sevenDays[0], sevenDays[7], API_KEY)
                val cachedAsteroidList = parseAsteroidsJsonResult(JSONObject(sevenDaysResult))

                MainViewModel.AsteroidStatus.SUCCESS

                /*need to resolve the AsteroidApi deferred<String> issue to clean up the .await() here???
                to get the asDatabaseModel to work, make getNextSevenDay public, then ensure NetworkAsteroid is used in the
                Data transfer object, also ensure it is a <List> and returns maps rather than an asteroid map
                 */
                database.asteroidDao.insertAll(*cachedAsteroidList.asDatabaseModel())

            } catch (e: Exception) {
                Log.i("ERROR MESSAGE", "Could not refresh the asteroid list: ${e.message}")
                MainViewModel.AsteroidStatus.FAILED

            }
        }
    }
}