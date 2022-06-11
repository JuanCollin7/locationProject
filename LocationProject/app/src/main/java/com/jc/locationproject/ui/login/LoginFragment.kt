package com.jc.locationproject.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jc.locationproject.database.AppDatabase
import com.jc.locationproject.databinding.FragmentLoginBinding
import com.jc.locationproject.ui.locations.LocationsActivity

class LoginFragment : Fragment() {

    private val loginButton by lazy { binding.loginButton }

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupTextViews()
        setupButton()
        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTextViews() {
        binding.usernameEditText.addTextChangedListener {
            viewModel.setUsername(it.toString())
        }

        binding.passwordEditText.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }
    }

    private fun setupButton() {
        loginButton.setOnClickListener {
            viewModel.login()
        }
    }

    private fun setupObservers() {
        viewModel.username.observe(viewLifecycleOwner) {
            loginButton.isEnabled = viewModel.mustEnableLoginButton()
        }
        viewModel.password.observe(viewLifecycleOwner) {
            loginButton.isEnabled = viewModel.mustEnableLoginButton()
        }
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it.didLogin) {
                didLogin(it.isAdmin)
            } else {
                Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun didLogin(isAdmin: Boolean) {
        if (isAdmin) {
            //TODO: Go to admin page
        } else {
            startActivity(LocationsActivity.getInstance(requireContext()))
        }
    }
}