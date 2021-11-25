package com.novatc.ap_app.fragments

import com.novatc.ap_app.Firestore.Fireclass
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
import kotlinx.android.synthetic.main.fragment_event_create.view.createEvent
import kotlinx.android.synthetic.main.fragment_event_create.view.createEventName
import com.novatc.ap_app.model.Event
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class EventCreateFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var dateButton: Button
    private var eventDate = "01-01-2021"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_create, container, false)
        initDatePicker(view)
        dateButton = view.createEventDate
        view.createEvent.setOnClickListener { onCreateEvent(view) }
        return view
    }

    private fun onCreateEvent(view: View) {
        val eventName = view.createEventName.text.toString().trim()
        val eventText = view.createEventText.text.toString().trim()

        if (eventName.isBlank() || eventText.isBlank() || eventDate.isBlank()) {
            Toast.makeText(context!!, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }
        if (dateIsInPast(eventDate)) {
            Toast.makeText(context!!, "Event date is in the past.", Toast.LENGTH_SHORT).show()
            return
        }
        val event = Event(
            eventName,
            eventDate,
            Fireclass().getCurrentUserID(),
            eventText
        )
        Fireclass().addEvent(event)
        Toast.makeText(requireActivity(), "Event created", Toast.LENGTH_SHORT).show()
        parentFragmentManager.commit {
            replace(R.id.fragment_container, EventFragment())
        }
    }

    private fun dateIsInPast(date: String): Boolean {
        val localDate = LocalDate.parse(date)
        return localDate!!.isBefore(LocalDate.now(ZoneId.systemDefault()))
    }

    private fun initDatePicker(view: View) {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        view.createEventDate.setOnClickListener {
            DatePickerDialog(context!!, this, year, month, day).show()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d("Datepicker", "Jahr: $year Monat: $month Tag: $dayOfMonth")
        var monthString = (month + 1).toString()
        var dayOfMonthString = dayOfMonth.toString()
        if (month < 10) {
            monthString = "0$month"
        }
        if (dayOfMonth < 10) {
            dayOfMonthString = "0$dayOfMonth"
        }
        val displayDateString = "$dayOfMonthString-$monthString-$year"
        val internalDateString = "$year-$monthString-$dayOfMonthString"
        dateButton.text = displayDateString
        eventDate = internalDateString
    }
}