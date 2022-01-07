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
import android.widget.Button
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
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
import com.novatc.ap_app.viewModels.CreateRoomViewModel
import kotlinx.android.synthetic.main.fragment_event_create.view.*


@AndroidEntryPoint
class RoomCreateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private val createRoomViewModel: CreateRoomViewModel  by viewModels()
    private var imageUri: Uri? = null
    private lateinit var imgProfile: ImageView
    lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>

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
                imgProfile = view.imageView2
                imageUri = fileUri
                imgProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.d("Error", "Error")
            } else {
                Log.d("Error", "Error")
            }
        }
        super.onCreate(savedInstanceState)
        initTimePicker(view)
        setPermissions(view)
        setOnCreateRoom(view)
        return view
    }

    private fun setOnCreateRoom(view: View) {
        view.btn_create_room.setOnClickListener {
            val roomName = view.et_created_room_name.text.toString().trim()
            val roomAddress = view.et_created_room_address.text.toString().trim()
            val roomDescription = view.et_created_room_description.text.toString().trim()
            val minimumBookingTime = view.et_created_room_booking_time.text.toString().trim()

            if (!isFormValid(
                    roomName,
                    roomAddress,
                    roomDescription,
                    minimumBookingTime,
                )
            ) return@setOnClickListener

            try {
                createRoomViewModel.addRoom(
                    roomName,
                    roomAddress,
                    roomDescription,
                    minimumBookingTime,
                    imageUri
                )
                Toast.makeText(
                    requireActivity(),
                    R.string.create_event_event_created_message,
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireActivity(),
                    R.string.create_event_event_not_created_message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            val action = RoomCreateFragmentDirections.actionRoomCreateFragmentToRoomFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun setPermissions(view: View) {
        val permissionsButton: Button = view.button
        permissionsButton.setOnClickListener {
            Permissions().checkPermissions(this)
            //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //resultLauncher.launch(intent)
            ImagePicker.with(this)
                .compress(1024)
                .cropSquare()//Final image size will be less than 1 MB(Optional)
                .maxResultSize(400, 400)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }

    private fun initTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        view.et_created_room_booking_time.setOnClickListener {
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }

    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        Log.d("Datepicker", "Stunde: $hour Minute: $minute")
        var hourString = hour.toString()
        var minuteString = minute.toString()
        var resultTime = hourString + ":" + minuteString
        et_created_room_booking_time.text = resultTime
    }

    private fun isFormValid(
        roomName: String,
        roomDescription: String,
        roomAddress: String,
        minimumBookingTime: String,
    ): Boolean {
        if (roomName.isBlank()
            || roomDescription.isBlank()
            || roomAddress.isBlank()
            || minimumBookingTime.isBlank()
        ) {
            Toast.makeText(context!!, R.string.create_event_required_field, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}

