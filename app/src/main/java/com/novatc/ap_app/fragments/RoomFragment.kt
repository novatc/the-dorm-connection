package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.RoomsAdapter
import com.novatc.ap_app.viewModels.RoomViewModel

class RoomFragment : Fragment() {
    private lateinit var layoutManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        fillRoomsList(view)
        val addRoomButton: FloatingActionButton = view.findViewById(R.id.btn_addRoomsButton)
        addRoomButton.setOnClickListener {
            val roomCreateFragment: Fragment = RoomCreateFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.fragment_container, roomCreateFragment)
            }
        }
        return view
    }

    private fun fillRoomsList(view: View) {
        val recyclerView: RecyclerView = view.findViewById((R.id.available_rooms))
        val model = ViewModelProvider(this)[RoomViewModel::class.java]
        model.rooms.observe(this, { rooms ->
            recyclerView.adapter = RoomsAdapter(rooms)
        })
        recyclerView.layoutManager = layoutManager
    }

}


