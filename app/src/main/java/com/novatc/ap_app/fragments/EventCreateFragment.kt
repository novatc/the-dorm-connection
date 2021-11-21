package com.novatc.ap_app.fragments

import Firestore.Fireclass
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_event_create.view.*
import kotlinx.android.synthetic.main.fragment_event_create.view.btn_safe_new_event
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import model.Event
import model.Room
import java.time.Year
import java.util.*


class EventCreateFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    lateinit var dateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_create, container, false)
        initDatePicker(view)
        dateButton = view.datePickerButton
            view.btn_safe_new_event.setOnClickListener {
            val eventList = EventFragment()
            val event = Event(
                view.eventName.text.toString(),
                "18.02.2021",
                Fireclass().getCurrentUserID(),
                "Drink beer"
            )
            Fireclass().addEventToDD(event)
            Toast.makeText(requireActivity(), "Event created", Toast.LENGTH_SHORT).show()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, eventList)
            }
        }


        return view
    }

    fun initDatePicker(view: View) {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        view.datePickerButton.setOnClickListener {
            DatePickerDialog(context!!, this, year, month, day).show()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d("Datepicker", "Jahr: $year Monat: $month Tag: $dayOfMonth")
        var monthString = month.toString()
        var dayOfMonthString = dayOfMonth.toString()
        if(month < 10)  {
            monthString = "0$month"
        }
        if (dayOfMonth < 10) {
            dayOfMonthString = "0$dayOfMonth"
        }
        dateButton.text = "$dayOfMonthString-$monthString-$year"
    }
}