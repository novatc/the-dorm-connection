package com.novatc.ap_app.activities.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.activities.adapter.RoomsAdapter
import model.EventListItem
import model.RoomsListItem

val roomsListItems: ArrayList<RoomsListItem> =  ArrayList()
val exampleroomName = "Pinte 42"
val exampleRoomTagline = "Pintenmittwoch "
val exampleRoomDescription = "Join us every Wednesday with your study budies to grab a couple of beers for cheap :)"
/**
 * A simple [Fragment] subclass.
 * Use the [RoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (roomsListItems.isEmpty()) {
            for (i in 0..5) {
                roomsListItems.add(
                    RoomsListItem(
                    exampleroomName,
                    exampleRoomTagline + i,
                    exampleRoomDescription,
                )
                )
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view =  getView() as View
        val recyclerView: RecyclerView = view.findViewById((R.id.upcoming_events))
        recyclerView.setHasFixedSize(true)
        val adapter = RoomsAdapter(roomsListItems)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

}