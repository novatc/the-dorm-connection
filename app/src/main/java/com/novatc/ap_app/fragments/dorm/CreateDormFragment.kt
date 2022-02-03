package com.novatc.ap_app.fragments.dorm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.repository.DormRepository
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.dorm.CreateDormViewModel
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
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,R.string.new_dorm_fields_required, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
        }
        lifecycleScope.launch {
            dormRepository.add(dormName,dormDescription,dormAddress)
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,R.string.new_dorm_added, Snackbar.LENGTH_SHORT).apply {
                anchorView = bottomNavView
            }.show()
        }
    }


}