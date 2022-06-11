package com.jc.locationproject.ui.admin.userLocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jc.locationproject.databinding.FragmentLocationsBinding
import com.jc.locationproject.databinding.FragmentUsersBinding
import com.jc.locationproject.ui.locations.LocationsAdapter

class UserLocationsFragment : Fragment() {

    private var _binding: FragmentLocationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = LocationsAdapter()

    companion object {
        fun newInstance() = UserLocationsFragment()
    }

    private val viewModel: UserLocationsViewModel by lazy {
        ViewModelProvider(requireActivity())[UserLocationsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.locationList.adapter = adapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            locations.let {
                adapter.data = locations
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}