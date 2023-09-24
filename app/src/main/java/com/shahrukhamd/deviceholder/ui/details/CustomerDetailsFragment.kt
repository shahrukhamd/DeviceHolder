package com.shahrukhamd.deviceholder.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.shahrukhamd.deviceholder.databinding.FragmentCustomerDetailsBinding
import com.shahrukhamd.deviceholder.ui.base.EventObserver
import com.shahrukhamd.deviceholder.ui.list.CustomerViewModel


class CustomerDetailsFragment: Fragment(), OnMapReadyCallback {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentCustomerDetailsBinding

    private val args: CustomerDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCustomerDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            customer = args.customer
            viewModel = customerViewModel
        }

        (childFragmentManager.findFragmentById(com.shahrukhamd.deviceholder.R.id.map_fragment) as SupportMapFragment).getMapAsync(this)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        args.customer?.let {
            val position = LatLng(it.currentLatitude ?: 0.0, it.currentLongitude ?: 0.0)
            googleMap.addMarker(MarkerOptions()
                .position(position)
                .title(it.firstName))

            googleMap.moveCamera(CameraUpdateFactory
                .newLatLng(LatLng(it.currentLatitude ?: 0.0, it.currentLongitude ?: 0.0)))
        }
    }

    private fun initObserver() {
        customerViewModel.closeCustomerDetails.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })
    }
}