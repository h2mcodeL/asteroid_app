package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    // get image of the day
    @Query("SELECT * FROM databaseimageofday")
    fun getImageOfDay(): LiveData<DatabaseImageOfDay>

    //get asteroids, return all items, in a list sorted by descending order
    @Query("SELECT * FROM databaseasteroid ORDER BY date(closeApproachDate) ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>        //this uses the asteroid database item

    //get asteroids, start date now, enddate 7 days from start date.
    @Query("SELECT * FROM databaseasteroid WHERE date(closeApproachDate) BETWEEN date('now') AND date('now','+7 days') ORDER BY date(closeApproachDate) ASC")
    fun getWeeklyAsteroid(): LiveData<List<DatabaseAsteroid>>

    //this is for TODAY
      @Query("SELECT * FROM databaseasteroid WHERE date(closeApproachDate)=date('now')")
      fun getTodayAsteroid(): LiveData<List<DatabaseAsteroid>>

     // @Query("SELECT * FROM databaseasteroid WHERE date(closeApproachDate) >= date('now') ORDER BY date(closeApproachDate) DESC ")
     // fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    //this is used to insert the asteroids into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)         //this needs to use the same DatabaseAsteroid as the @query above

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(vararg imageOfDay: DatabaseImageOfDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabaseImageOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

//define an instance variable to store the singleton
private lateinit var INSTANCE: AsteroidDatabase

//this is the public way to access the database, using getDatabase
fun getDatabase(context: Context) : AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {            //we wrap the if statement using synchronised to ensure it is thread safe.
        if(!::INSTANCE.isInitialized) {     //this states that if the singleton is not initialised
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }

    return INSTANCE
}
