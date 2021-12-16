package com.novatc.ap_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.UserAdapter
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.EventRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.event_details_fragment.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class EventDetailsFragment : Fragment() {
    private val args by navArgs<EventDetailsFragmentArgs>()
    var userList: ArrayList<User> = ArrayList()
    @Inject
    lateinit var  userRepository: UserRepository
    @Inject
    lateinit var  eventRepository: EventRepository


    var me: User = User()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.event_details_fragment, container, false)
        lifecycleScope.launch {
            me = userRepository.readCurrent()!!
            if (userList.contains(me)){
                Log.e("EVENT", "ALREADY JOINED")
                view.btn_join_event.visibility = View.GONE
            }
        }
        Log.e("EVENT", "Me: ${me}")
        val event = args.selectedEvent
        view.event_name.text = event.name
        view.tv_event_description.text  = event.text
        view.tv_event_date.text = event.date
        userList = event.userList

        view.btn_join_event.setOnClickListener {
            userJoinsEvent()
        }

        populateUserList(view)

        return view
    }

    private fun populateUserList(view: View){
        val recyclerView: RecyclerView = view.rv_user_list
        recyclerView.adapter= UserAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun userJoinsEvent(){
        lifecycleScope.launch {
            val currentUser = userRepository.readCurrent()
            if (currentUser != null) {
                args.selectedEvent.userList.add(currentUser)
                userList.add(currentUser)
            }
        }

        args.selectedEvent.id?.let { eventRepository.updateUserList(userList, it) }

    }


}