# Nasa Asteroid
This app pulls data on near earth asteroids from the Nasa NeoWs website and presents them in a list using the recyclerView.
Nasa also post an image of the day, which is rotated daily on the home screen with the image description.

Selecting an asteroid within the list exposes further details, listing the following details.

- The app uses the MVVM architecture with databinding.
- Room and a repository to cache and present asteroids based on their date selection.
- Work manager to schedule background tasks.
-

## Prerequisites
Android SDK v30 Android Build Tools v29.0.3

To use the app, you will require an API key from NASA. Navigate to the https://api.nasa.gov website, where you can generate a key if you choose to fork this project.