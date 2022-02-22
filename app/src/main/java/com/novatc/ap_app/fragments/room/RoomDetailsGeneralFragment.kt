package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.BookingListAdapter
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.getTimezone
import com.novatc.ap_app.model.Booking
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.model.User
import com.novatc.ap_app.viewModels.room.RoomDetailsViewModel
import kotlinx.android.synthetic.main.fragment_room_details_general.view.*
import kotlinx.android.synthetic.main.fragment_room_details_general.view.tutView1
import kotlinx.android.synthetic.main.fragment_room_details_general.view.tutView2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


class RoomDetailsGeneralFragment : Fragment(), BookingListAdapter.OnItemClickListener {
    val model: RoomDetailsViewModel by activityViewModels()
    private var currentUser: User = User()
    lateinit var bookingListOnRoom: ArrayList<Booking>
    lateinit var selectedRoom: Room
    private var timezoneOffset = 0L
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_details_general, container, false)
        bookingListOnRoom = ArrayList()
        startObservers(view)
        setBackButtonListener(view)
        setBookingButtonListener(view)
        timezoneOffset = getTimezone()
        recyclerView = view.rec
        return view
    }

    @ExperimentalCoroutinesApi
    fun populateBookingsList() {
        val model: RoomDetailsViewModel by activityViewModels()
        model.userProfile.observe(viewLifecycleOwner) {
            currentUser = it

            val bookingAdapter = BookingListAdapter(this)
            recyclerView.adapter = bookingAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)

            model.bookings.observe(viewLifecycleOwner) { booking ->
                booking.forEach { singleBooking ->
                    if (singleBooking.userID == currentUser.id && !alreadyIsInBookingsList(singleBooking)
                    ) {
                        singleBooking.startingDate = singleBooking.startingDate + timezoneOffset
                        singleBooking.endDate = singleBooking.endDate + timezoneOffset
                        bookingListOnRoom.add(singleBooking)
                    }
                }
                bookingListOnRoom.sortedByDescending { it.startingDate }
                bookingListOnRoom.reversed()
                bookingAdapter.differ.submitList(bookingListOnRoom)
            }
        }
    }

    private fun alreadyIsInBookingsList(booking: Booking): Boolean{
        bookingListOnRoom.forEach{
            currentBooking ->
            if(currentBooking.id == booking.id){
                return true
            }
        }
        return false
    }

    private fun startObservers(view: View){
        model.room.observe(this, { room ->
            selectedRoom = room
            view.room_details_description.text = room.description
        })
        model.room.observe(this, {
            model.loadBookings(null)
        })
    }

    override fun onItemClick(position: Int) {
        val booking = bookingListOnRoom[position]
        lifecycleScope.launch {
            selectedRoom.id?.let {
                model.deleteBooking(
                    bookingID = booking.id,
                    it
                )
            }
        }
    }

    private fun setBookingButtonListener(view: View){
        view.button_bookings.setOnClickListener {
            switchView("view2" , view)
            populateBookingsList()  }
    }

    private fun setBackButtonListener(view: View){
        view.button_back_to_description.setOnClickListener {
            switchView("view1" , view)
        }
    }

    //switches between calendar view and booking view
    private fun switchView(viewName: String, view: View){
        if(viewName == "view2"){
            val layout = view.tutView1
            layout.visibility = View.GONE
            val layout2 = view.tutView2
            layout2.visibility = View.VISIBLE
        }
        else if(viewName == "view1"){
            val layout = view.tutView1
            layout.visibility = View.VISIBLE
            val layout2 = view.tutView2
            layout2.visibility = View.GONE
        }
    }
}