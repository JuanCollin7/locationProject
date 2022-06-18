package com.jc.locationproject.ui.locations

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jc.locationproject.services.LocationBroadcastReceiver
import com.jc.locationproject.services.LocationService
import com.jc.locationproject.databinding.FragmentLocationsBinding

class LocationsFragment : Fragment() {

    private var _binding: FragmentLocationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = LocationsAdapter()

    private val locationReceiver = LocationBroadcastReceiver(::onLocationReceived)

    companion object {
        fun newInstance() = LocationsFragment()
    }

    private val viewModel: LocationsViewModel by lazy {
        ViewModelProvider(this)[LocationsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.locationList.adapter = adapter

        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestBackgroundLocationPermission()

        startLocationBroadcast()

        requireContext().startService(Intent(requireContext(), LocationService::class.java))

        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            locations.let {
                adapter.data = locations
            }
        }
    }

    private fun startLocationBroadcast() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            locationReceiver,
            IntentFilter(LocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    private fun onLocationReceived(location: Location) {
        viewModel.newLocation(location)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestBackgroundLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.v("PERMISSION", "BACKGROUND")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //override fun onItemClicked(item: Post) {
    //    startActivity(NEXTACTIVITY.getInstance(requireContext(), item))
    //}
}