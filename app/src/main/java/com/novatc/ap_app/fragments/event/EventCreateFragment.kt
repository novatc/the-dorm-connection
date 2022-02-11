package com.novatc.ap_app.fragments.event

import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_event_create.view.*
import kotlinx.android.synthetic.main.fragment_event_create.view.createEvent
import kotlinx.android.synthetic.main.fragment_event_create.view.createEventName
import com.novatc.ap_app.viewModels.event.CreateEventViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import android.widget.ImageView

import androidx.annotation.NonNull
import androidx.core.graphics.drawable.toDrawable


@AndroidEntryPoint
class EventCreateFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var dateButton: Button
    private var eventDate = "2022-01-01"
    private val createEventViewModel: CreateEventViewModel by viewModels()
    private var imageUri: Uri? = null
    private var imageSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_create, container, false)
        initDatePicker(view)
        dateButton = view.createEventDate
        setupOnCreateEventListener(view)
        setupOnSelectImageListener(view)
        return view
    }

    // Checks form on submit and creates a new event
    private fun setupOnCreateEventListener(view: View) {
        view.createEvent.setOnClickListener {
            val eventName = view.createEventName.text.toString().trim()
            val eventText = view.createEventText.text.toString().trim()
            val eventStreet = view.textStreetName.text.toString().trim()
            val eventHouseNumber = view.textHouseNumber.text.toString().trim()
            val eventCity = view.textCity.text.toString().trim()

            if (!isFormValid(
                    eventName,
                    eventText,
                    eventDate,
                    eventStreet,
                    eventHouseNumber,
                    eventCity
                )
            ) return@setOnClickListener

            try {
                createEventViewModel.addEvent(
                    eventName,
                    eventDate,
                    eventText,
                    eventStreet,
                    eventHouseNumber,
                    eventCity,
                    imageUri
                )
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.create_event_event_created_message, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
            } catch (e: Exception) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.create_event_event_not_created_message, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
            view.findNavController()
                .navigate(EventCreateFragmentDirections.actionEventCreateFragmentToEventFragment())
        }
    }

    // Listener for when user has selected an image
    private fun setupOnSelectImageListener(view: View) {
        val imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> imageSelected(result.data?.data, view)
                else -> Log.i("CreateEvent", "No image selected.")
            }
        }
        view.eventCreateSelectImageButton.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .cropSquare()
                .maxResultSize(400, 400)
                .createIntent { intent -> imagePickerResult.launch(intent) }
        }

    }

    private fun imageSelected(uri: Uri?, view: View) {
        if (uri != null) {
            view.eventCreateImage.setImageURI(uri)
            imageUri = uri
            imageSelected = true
        }
    }

    private fun isFormValid(
        eventName: String,
        eventText: String,
        eventDate: String,
        eventStreet: String,
        eventHouseNumber: String,
        eventCity: String
    ): Boolean {
        if (eventName.isBlank()
            || eventText.isBlank()
            || eventDate.isBlank()
            || eventStreet.isBlank()
            || eventHouseNumber.isBlank()
            || eventCity.isBlank()
        ) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView, R.string.create_event_required_field, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        if (!imageSelected){
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView, "Image required!", Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        if (dateIsInPast(eventDate)) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView, R.string.create_event_date_past, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        return true
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
        var monthString = (month + 1).toString()
        var dayOfMonthString = dayOfMonth.toString()
        if (month < 10) {
            monthString = "0$monthString"
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