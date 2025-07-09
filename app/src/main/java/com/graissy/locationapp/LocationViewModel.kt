package com.graissy.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    // Here we're overwriting this '_location' ↑↑ with whatever the location is once we get the 'locationResult'
    // in 'requestLocationUpdates' in 'LocalUtils' class
    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }
}