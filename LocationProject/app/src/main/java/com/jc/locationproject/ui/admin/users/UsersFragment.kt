package com.jc.locationproject.ui.admin.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jc.locationproject.core.ItemClickListener
import com.jc.locationproject.databinding.FragmentUsersBinding
import com.jc.locationproject.models.User
import com.jc.locationproject.ui.admin.userLocations.UserLocationsActivity

class UsersFragment : Fragment(), ItemClickListener<User> {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = UsersAdapter(this)

    companion object {
        fun newInstance() = UsersFragment()
    }

    private val viewModel: UsersViewModel by lazy {
        ViewModelProvider(this)[UsersViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.userList.adapter = adapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.users.observe(viewLifecycleOwner) { locations ->
            locations.let {
                adapter.data = locations
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: User) {
        startActivity(UserLocationsActivity.getInstance(requireContext(), item))
    }
}