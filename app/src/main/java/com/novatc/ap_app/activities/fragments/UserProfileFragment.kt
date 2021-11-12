package com.novatc.ap_app.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.activities.EditProfileActivity
import com.novatc.ap_app.activities.activities.EditWGActivity
import com.novatc.ap_app.activities.activities.SignUpActivity
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_edit_profile.setOnClickListener {
            val intent = Intent (requireContext(), EditWGActivity::class.java)
            activity?.startActivity(intent)
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user_profile, container, false)
        view.btn_edit_profile.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, EditProfileActivity::class.java))
                finish()
            }
        }
        view.btn_edit_wg.setOnClickListener{
            val intent = Intent (activity, EditWGActivity::class.java)
            activity?.startActivity(intent)
        }
        view.btn_delet_profile.setOnClickListener{
            val intent = Intent (activity, SignUpActivity::class.java)
            activity?.startActivity(intent)
            Toast.makeText(requireContext(), "Your Profile has been deleted", Toast.LENGTH_LONG).show()
        }
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}