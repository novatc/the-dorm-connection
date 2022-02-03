package com.novatc.ap_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.User
import com.novatc.ap_app.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val model: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        setOnLoginListener(view)
        setOnSwitchToSignUpListener(view)
        model.loadUserObject()
        model.me.observe(this, {
            navigateIfAlreadyLoggedIn(it)
        })
        return view
    }

    private fun navigateIfAlreadyLoggedIn(user: User) {
        if (model.getCurrentFirebaseUser() != null && user.userDormID != "") {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFragmentPinboard())
        }
        if(model.getCurrentFirebaseUser() != null && user.userDormID==""){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChooseDormFragment())
        }
    }

    /**
     * Switches to sign up fragment on button press
     * @param view
     */
    private fun setOnSwitchToSignUpListener(view: View) {
        view.button_to_signup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }

    /**
     * Checks if no field is empty and logs the user in on button press
     * @param view
     */
    private fun setOnLoginListener(view: View) {
        view.button_login.setOnClickListener {
            val email: String = text_login_email.text.toString().replace(" ", "")
            val password: String = text_login_password.text.toString().replace(" ", "")

            if (email.isEmpty() || password.isEmpty()) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.empty_field_error, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
                return@setOnClickListener
            }
            model.loginUser(email, password)
            model.loginRequest.observe(this, { request ->
                when (request.status) {
                    Request.Status.SUCCESS -> findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToFragmentPinboard()
                    )
                    else -> {
                        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                        Snackbar.make(bottomNavView, R.string.login_error, Snackbar.LENGTH_LONG).apply {
                            anchorView = bottomNavView
                        }.show()
                    }

                }
            })
        }
    }

}