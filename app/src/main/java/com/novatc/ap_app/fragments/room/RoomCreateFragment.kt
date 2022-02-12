package com.novatc.ap_app.fragments.room

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TimePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import com.novatc.ap_app.permissions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_create.*
import java.util.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.addZeroToShortNumber
import com.novatc.ap_app.viewModels.room.CreateRoomViewModel
import kotlinx.android.synthetic.main.fragment_event_create.view.*
import kotlinx.android.synthetic.main.fragment_room_details_book.view.*


@AndroidEntryPoint
class RoomCreateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private val createRoomViewModel: CreateRoomViewModel by viewModels()
    private var imageUri: Uri? = null
    private lateinit var imgProfile: ImageView
    lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>
    var minimumBookingTimeInMillis = 0L
    var maximumBookingTimeInMillis = 0L
    var chosenPicker = ""
    private var imageSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_create, container, false)
        startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                imgProfile = view.room_create_image_view
                imageUri = fileUri
                imgProfile.setImageURI(fileUri)
            }
        }
        super.onCreate(savedInstanceState)
        initTimePicker(view)
        setupOnSelectImageListener(view)
        setOnCreateRoom(view)
        return view
    }

    private fun setOnCreateRoom(view: View) {
        view.btn_create_room.setOnClickListener {
            val roomName = view.et_created_room_name.text.toString().trim()
            val streetName = view.et_street_name.text.toString().trim()
            val houseNumber = view.et_house_number.text.toString().trim()
            val city = view.et_city.text.toString().trim()
            val roomShortDescription = view.et_created_room_short_description.text.toString().trim()
            val roomDescription = view.et_created_room_description.text.toString().trim()
            val minimumBookingTime = minimumBookingTimeInMillis.toString()
            val maximumBookingTime = maximumBookingTimeInMillis.toString()

            if (!isFormValid(
                    roomName,
                    roomShortDescription,
                    roomDescription,
                    minimumBookingTime,
                    maximumBookingTime
                )
            ) return@setOnClickListener

            try {
                createRoomViewModel.addRoom(
                    roomName,
                    streetName,
                    houseNumber,
                    city,
                    roomShortDescription,
                    roomDescription,
                    minimumBookingTime,
                    maximumBookingTime,
                    imageUri
                )
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.create_event_event_created_message, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
            } catch (e: Exception) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.create_event_event_not_created_message, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
            }
            val action = RoomCreateFragmentDirections.actionRoomCreateFragmentToRoomFragment()
            view.findNavController().navigate(action)
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
        view.upload_room_image_button.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .cropSquare()
                .maxResultSize(400, 400)
                .createIntent { intent -> imagePickerResult.launch(intent) }
        }

    }

    private fun imageSelected(uri: Uri?, view: View) {
        if (uri != null) {
            view.room_create_image_view.setImageURI(uri)
            imageUri = uri
            imageSelected = true
        }
    }

    private fun initTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        view.et_created_room_minimum_booking_time.setOnClickListener {
            chosenPicker = "min"
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }
        view.et_created_room_maximum_booking_time.setOnClickListener {
            chosenPicker = "max"
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }

    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val hourString = addZeroToShortNumber(hour.toString(), false)
        val minuteString = addZeroToShortNumber(minute.toString(), false)
        val resultTime = "$hourString:$minuteString"
        if(chosenPicker == "min"){
            minimumBookingTimeInMillis = timeToMillis(hour, minute)
            et_created_room_minimum_booking_time.text = resultTime
        }
        else{
            maximumBookingTimeInMillis = timeToMillis(hour, minute)
            et_created_room_maximum_booking_time.text = resultTime
        }
    }

    private fun isFormValid(
        roomName: String,
        roomShortDescription: String,
        roomDescription: String,
        minimumBookingTime: String,
        maximumBookingTime: String
    ): Boolean {
        if (roomName.isBlank()
            || roomDescription.isBlank()
            || roomShortDescription.isBlank()
            || minimumBookingTime.isBlank()
            || maximumBookingTime.isBlank()
        ) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,  R.string.empty_all_field_error, Snackbar.LENGTH_SHORT).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        if (!imageSelected){
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,R.string.image_required , Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        if(roomShortDescription.length > 40){
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,R.string.short_description_error , Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        return true
    }

    private fun timeToMillis(hour: Int, minute: Int): Long{
        return (hour * 60 * 60 * 1000 + minute * 60 * 1000).toLong()
    }

}

