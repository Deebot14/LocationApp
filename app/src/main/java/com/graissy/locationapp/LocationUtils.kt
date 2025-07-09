package com.graissy.locationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient
    = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    // We use this function in order to check if we have access to the location features of Android
    // with our application on the User device or not
    fun hasLocationPermission(context: Context): Boolean{
        // 'checkSelfPermission()' will return an int but we need to return a boolean
        // We are comparing if the int from 'checkSelfPermission()' is the same as in 'PackageManager.PERMISSION_GRANTED'
        // for 'ACCESS_FINE_LOCATION' and 'ACCESS_COARSE_LOCATION'
        // This will check if you gave access on your device or not
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED


    }

    // Make a lat-long location into a readable address
    fun reverseGeocodeLocation(location: LocationData): String{
        // We setup a geocoder with the current context and whatever Locale Setting we have
        // The locale is passed to the Geocoder to define how the address should be formatted
        // in terms of language and regional nuances.
        val geocoder = Geocoder(context, Locale.getDefault())
        // We get the Coordinates (latitude, longitude)
        val coordinates = LatLng(location.latitude, location.longitude)
        // We load the addresses as a mutable list
        // And with using our geocoder we get from the coordinates latitude and longitude '1' result
        // 'getFromLocation()' returns a list of Address objects based on the provided latitude and longitude
        val addresses: MutableList<Address>? =
            geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        // We check if 'addresses' is not empty then get the first address inside of 'addresses'
        return if(addresses?.isNotEmpty() == true){
            addresses[0].getAddressLine(0)
        }else{
            "Address Not found"
        }
    }

}