# LocationApp
This is a personal project to practice working with location services in Android — a simple app that fetches and displays the user's current location. It shows the latitude, longitude, and a human-readable address by using reverse geocoding. The app demonstrates permission handling, location updates, and data binding using Jetpack Compose.

## Programming Languages / Technologies
Kotlin, Android SDK, Jetpack Compose, FusedLocationProviderClient (Google Play Services), ViewModel, LiveData (Compose State), Permissions API, Geocoder, Material Design Components

## Features
- ### Real-Time Location Fetching
Uses FusedLocationProviderClient to request the user’s current location with high accuracy.

- ### Reverse Geocoding
Converts latitude and longitude into a readable address using Geocoder.

- ### Permission Handling
Manages runtime permissions (ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION) with proper rationale display.

- ### Composable UI
Built entirely using Jetpack Compose, with a clean interface that includes a button to trigger location updates.

- ### Location ViewModel
Stores the user’s current location in a shared state using a ViewModel and mutableStateOf.

- ### User Feedback
Shows messages when permissions are denied or not yet granted, ensuring clear communication with the user.


