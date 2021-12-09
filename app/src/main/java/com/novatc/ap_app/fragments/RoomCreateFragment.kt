package com.novatc.ap_app.fragments

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import kotlinx.android.synthetic.main.fragment_room_create.view.btn_save_room
import com.novatc.ap_app.permissions.*
import com.novatc.ap_app.repository.RoomRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_create.*
import java.util.*
import javax.inject.Inject
import android.app.Dialog
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.github.dhaval2404.imagepicker.ImagePicker


@AndroidEntryPoint
class RoomCreateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null
    private lateinit var imgProfile: ImageView

    @Inject
    lateinit var roomsRepository: RoomRepository
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
                mProfileUri = fileUri
                imgProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.d("Error", "Error")
            } else {
                Log.d("Error", "Error")
            }
        }
        super.onCreate(savedInstanceState)
        initTimePicker(view)
        setSaveRoomButtonListener(view)
        setPermissions(view)
        setDialog(true)
        return view
    }

    private fun onCreateRoom(view: View) {
        val roomName = view.created_room_name.text.toString().trim()
        val roomAddress = view.created_room_address.text.toString().trim()
        val roomDescription = view.created_room_description.text.toString().trim()
        var minimumBookingTime = view.created_room_booking_time.text.toString().trim()

//        if (roomName.isBlank() || roomAddress.isBlank() || roomDescription.isBlank() || !(minimumBookingTime.matches(Regex("\\d{2}-\\d{2}")))) {
//            Toast.makeText(context!!, "All fields are required.", Toast.LENGTH_SHORT).show()
//            return
//        }
        context?.let {
            roomsRepository.add(roomName, roomAddress, roomDescription, minimumBookingTime, (mProfileUri!!).toString(),
                it
            )
        }

        Toast.makeText(requireContext(), "Room created", Toast.LENGTH_SHORT).show()

    }

    private fun setSaveRoomButtonListener(view: View) {
        val saveRoomButton: Button = view.btn_save_room
        saveRoomButton.setOnClickListener {
            onCreateRoom(view)
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
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            when (requestCode) {

            }
        }
    }


    private fun initTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        view.created_room_booking_time.setOnClickListener {
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }

    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        Log.d("Datepicker", "Stunde: $hour Minute: $minute")
        var hourString = hour.toString()
        var minuteString = minute.toString()
        var resultTime = hourString + ":" + minuteString
        created_room_booking_time.text = resultTime
    }

    var dialog: Dialog? = null

    //   This method is used to control the progress dialog.
    private fun setDialog(show: Boolean) {
        if (show) dialog?.show() else dialog?.dismiss()
    }

}

