package com.novatc.ap_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

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
                Toast.makeText(activity, R.string.empty_field_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val model: SignUpViewModel by viewModels()
            model.signUpUser(name, email, password)
            model.signupRequest.observe(this, { request ->
                when (request.status) {
                    Request.Status.SUCCESS -> findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToFragmentPinboard()
                    )
                    else -> Toast.makeText(activity, R.string.sign_up_error, Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }

}
