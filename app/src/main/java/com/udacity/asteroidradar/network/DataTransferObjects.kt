package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabaseImageOfDay
import com.udacity.asteroidradar.domain.Asteroid


/** DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * of formatting objects to send to the server. These are converted to domain objects before using them.
 */

//we receive the network objects called NetworkAsteroidContainer from AsteroidApiService downloaded from the api website

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

//this is an extension function that converts from network results to database objects

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
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

//this converts from dataTransfer objects to database objects.
// we are looking for a list of asteroids so need a list item
   // fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {      //<Asteroid> {       //<DatabaseAsteroid> {
fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
   // return asteroids.map {
       // Asteroid(
        DatabaseAsteroid(
            id = it.id,
            codeName = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}

//create the data transfer for the picture of the day

data class NetworkImageOfTheDay(
    @Json(name = "media_type")val mediaType: String,
    val title: String,
    val url: String
)

fun NetworkImageOfTheDay.asDatabaseModel(): DatabaseImageOfDay {
    return DatabaseImageOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}