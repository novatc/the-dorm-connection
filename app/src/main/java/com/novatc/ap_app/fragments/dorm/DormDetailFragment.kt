package com.novatc.ap_app.fragments.dorm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dorm_detail.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DormDetailFragment : Fragment() {
    private val args by navArgs<DormDetailFragmentArgs>()

    @Inject
    lateinit var userRepository: UserRepository
    var me: User = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dorm_detail, container, false)
        lifecycleScope.launch {
            me = userRepository.readCurrent()!!
        }
        val dorm = args.selectedDorm
        view.tv_dorm_name.text = dorm.name
        view.tv_dorm_description.text = dorm.description
        view.tv_dorm_address.text = dorm.address
        val users = dorm.userList
        view.tv_dorm_users.text = users.size.toString()
        if (me.userDorm!=""){
            view.btn_choose_dorm.visibility = View.GONE
        }
        view.btn_choose_dorm.setOnClickListener {
            lifecycleScope.launch {
                me.userDorm = dorm.name.toString()
                me.userDormID = dorm.id.toString()
                userRepository.updateUserWithDorm(me)

            }
            val action = DormDetailFragmentDirections.actionDormDetailFragmentToFragmentPinboard()
            findNavController().navigate(action)
        }

        return view
    }


}