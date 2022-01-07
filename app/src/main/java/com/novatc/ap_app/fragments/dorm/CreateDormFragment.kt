package com.novatc.ap_app.fragments.dorm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.novatc.ap_app.R
import com.novatc.ap_app.repository.DormRepository
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.CreateDormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_dorm.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateDormFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var dormRepository: DormRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_dorm, container, false)



        view.btn_save_dorm.setOnClickListener {
            onDormCreated(view)
            val action = CreateDormFragmentDirections.actionCreateDormFragmentToChooseDormFragment()
            findNavController().navigate(action)
        }
        return view
    }

    fun onDormCreated(view: View) {
        val viewModel = ViewModelProvider(this)[CreateDormViewModel::class.java]

        val dormName = view.et_created_dorm_name.text.toString().trim()
        val dormDescription = view.et_created_dorm_description.text.toString().trim()
        val dormAddress = view.et_created_post_keywords.text.toString().trim()
        if (dormName.isBlank()|| dormDescription.isBlank()||dormAddress.isBlank()){
            Toast.makeText(requireContext(),"All fields are required.", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            dormRepository.add(dormName,dormDescription,dormAddress)
            Toast.makeText(requireContext(), "Dorm created", Toast.LENGTH_SHORT).show()
        }
    }


}