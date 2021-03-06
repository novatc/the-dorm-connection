package com.novatc.ap_app.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    val model: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        setOnRegisterListener(view)
        setOnSwitchToLoginListener(view)
        return view
    }

    /**
     * Switches to login fragment on button press
     * @param view
     */
    private fun setOnSwitchToLoginListener(view: View) {
        view.button_to_login.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }
    }

    /**
     * Checks if no field is empty and signs the user up on button press
     * @param view
     */
    private fun setOnRegisterListener(view: View) {
        view.button_sign_up.setOnClickListener {
            val name: String = text_sign_up_name.text.toString().replace(" ", "")
            val email: String = text_sign_up_email.text.toString().replace(" ", "")
            val password: String = text_sign_up_password.text.toString().replace(" ", "")
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                val buttonSignup: Button = activity?.findViewById(R.id.button_sign_up)!!
                Snackbar.make(buttonSignup, R.string.empty_field_error, Snackbar.LENGTH_SHORT).apply {
                    anchorView = buttonSignup
                }.show()
                return@setOnClickListener
            }
            model.signUpUser(name, email, password)
            observeSignupRequest()
        }
    }

    private fun observeSignupRequest() {
        model.signupRequest.observe(this, { request ->
            when (request.status) {
                Request.Status.SUCCESS -> findNavController().navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToChooseDormFragment()
                )
                else -> {
                    val buttonSignup: Button = activity?.findViewById(R.id.button_sign_up)!!
                    Snackbar.make(buttonSignup, R.string.sign_up_error, Snackbar.LENGTH_LONG).apply {
                        anchorView = buttonSignup
                    }.show()
                }
            }
        })
    }

}
