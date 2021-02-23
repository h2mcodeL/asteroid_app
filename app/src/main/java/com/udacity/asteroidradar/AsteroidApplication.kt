package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication: Application() {

    /**onCreate is called before the first screen is shown, this is where we setup our background
     ** thread to avoid slowing the app
     ** create a coroutine scope for use here in the main activity. Create a coroutine scope, then an initialization function
     ** that does not block the main thread.
     */

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    //initialization is removed from onCreate
    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    //    Be able to cache the data of the asteroid by using a worker, so it downloads and saves
//    today's asteroids in background once a day when the device is charging and wifi is enabled.


    private fun setupRecurringWork() {
        //add constraint for the worker, setting the times, battery use etc for the work request
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)      //only run on unmetered network,
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)  //when the device is charging

            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresStorageNotLow(true)
                }
            }.build()

        //now add constraints to the repeating request
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        //this is scheduled on a peroidic basis
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.Asteroid_Worker,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}
