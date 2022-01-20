package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import kotlinx.android.synthetic.main.fragment_room_details_book.view.*
import kotlinx.android.synthetic.main.fragment_room_details_general.view.*
import kotlin.properties.Delegates
import androidx.annotation.NonNull




class RoomDetailsBookFragment : Fragment() {
    val model: RoomDetailsViewModel by activityViewModels()
    lateinit var calendar: CalendarView
    var selectedDate by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_details_book, container, false)
        //calendar = view.calendarView
        selectedDate = calendar.date
        // Observe event and setup event description
        addDateListener(calendar)
        model.room.observe(this, {
        })
        return view
    }

    fun addDateListener(calendar: CalendarView){
        calendar
            .setOnDateChangeListener { view, year, month, dayOfMonth ->
                val Date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)
                val longDate = calendar.date
                print(Date)
                calendar.dateTextAppearance
            }
    }
}

private fun CalendarView.setOnDateChangeListener(selectedDate: Long) {

}
