package com.graissy.locationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.graissy.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    myApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun myApp(viewModel: LocationViewModel){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(locationUtils = locationUtils, viewModel, context = context)
}

// This function creates a button to update the User's Location
@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
) {

    // If we want to access the location in 'LocationViewModel' class we make a variable
    val location = viewModel.location.value

    // We get the address from 'location'
    // Then we unpack the location with 'let{}' and we get the 'LocationData'
    // Then we call 'locationUtils.reverseGeocodeLocation' that returns a String
    // This String will be set as the address
    val address = location?.let {
        locationUtils.reverseGeocodeLocation(location)
    }

    // The 'rememberLauncherForActivityResult' function is used to register a request to start an activity and handle the result,
    // which is crucial for handling permissions in the application.
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        // The permissions map contains the results of the location permission requests,
        // indicating whether each permission was granted or denied.
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // I HAVE ACCESS TO LOCATION

                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                // ASK FOR PERMISSION
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if(rationaleRequired){
                    Toast.makeText(context,
                        "Location Permission is required for this feature to work", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,
                        "Location Permission is required. Please enable it in the Android Settings", Toast.LENGTH_LONG).show()
                }

            }
        })

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(location != null){
            Text("Address: ${location.latitude} ${location.longitude} \n $address")
        }else{
            Text(text = "Location not available")
        }

        Button(onClick = {
            if(locationUtils.hasLocationPermission(context)){
                // Permission already granted - Update the location
                locationUtils.requestLocationUpdates(viewModel)
            }else{
                // Request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}









