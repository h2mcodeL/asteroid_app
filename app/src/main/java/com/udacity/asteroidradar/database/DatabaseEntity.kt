package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.domain.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey              //the primary key shall be constructed using asteroid id
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

//this extension function converts from a database object to domain objects
//the names used here must match the Asteroid entities layout.

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codeName = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

@Entity
data class DatabaseImageOfDay constructor(
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String
)

fun DatabaseImageOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}