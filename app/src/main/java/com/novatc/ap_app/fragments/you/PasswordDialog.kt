package com.novatc.ap_app.fragments.you

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.you.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_password.view.*
import kotlinx.android.synthetic.main.fragment_profile_options.view.*

@AndroidEntryPoint
class PasswordDialog(private val parentView: View) : DialogFragment() {

    val model: ProfileViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_password, null)
            builder.setView(view)
                .setPositiveButton(
                    R.string.confirm_button
                ) { _, _ ->
                    parentView.deleteUserProgress.visibility = View.VISIBLE
                    val password = view.dialogPassword.text.toString()
                    model.deleteUser(password)
                }
                .setNegativeButton(
                    R.string.cancel_button
                ) { _, _ ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}