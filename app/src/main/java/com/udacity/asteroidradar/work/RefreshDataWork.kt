package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    //create a companion function for the worker name

    companion object {
        const val Asteroid_Worker = "RefreshDataWorker"     //unique id for workschedule
    }

    override suspend fun doWork(): Result {
        //we use suspend as this is a Coroutine, do works runs in a background Thread. so will not block UI Thread
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshImageOfDay()
            repository.refreshAsteroids()

            //to let work manager know the search is complete.
            Result.success()
            //if a network request fails, do the following and retry later
        } catch (exception: HttpException) {
            Result.retry()
        }


        //create a WorkRequest
      /*  val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>().build()


        //submit the workrequest to the system, using enqueue()
        WorkManager.getInstance(myContext)
            .enqueue(uploadWorkRequest)

       */




    }
}