package com.novatc.ap_app.firestore

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.novatc.ap_app.R

class FirestoreMethods {
    companion object{
        lateinit var storage: FirebaseStorage
        var dialog: Dialog? = null

        fun uploadPicture(profileImg: Uri, context: Context){
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            builder.setView(R.layout.progress_bar)
            Looper.prepare()
            dialog = builder.create()
            storage = Firebase.storage
            val filename = profileImg.pathSegments[profileImg.pathSegments.lastIndex]

            val storageRef = FirebaseStorage.getInstance().getReference("images/$filename")

            var file = profileImg
            val uploadTask = storageRef.putFile(file)
            setDialog(true)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(context, R.string.image_upload_successful, Toast.LENGTH_SHORT).show()
                setDialog(false)
            }.addOnFailureListener() {
                Toast.makeText(context, "Image could not be uploaded", Toast.LENGTH_SHORT).show()
                setDialog(false)
            }
        }

        fun deletePicture(profileImg: String){
            storage = Firebase.storage
            val photoRef: StorageReference = FirebaseStorage.getInstance().getReference("images/$profileImg")
            photoRef.delete().addOnSuccessListener {  }.addOnFailureListener {  }
        }

        fun loadPicture(image: ImageView, profileImg: String, context: Context?){
            val storageRef = FirebaseStorage.getInstance().getReference("images/$profileImg")
            storageRef.downloadUrl.addOnSuccessListener { Uri ->
                val imageURL = Uri.toString()
                Glide.with(context!!)
                    .load(imageURL)
                    .into(image)
            }

        }

        private fun setDialog(show: Boolean) {
            if (show) dialog?.show() else dialog?.dismiss()
        }
    }
}