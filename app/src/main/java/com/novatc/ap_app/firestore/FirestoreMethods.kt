package com.novatc.ap_app.firestore

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.novatc.ap_app.R

class FirestoreMethods {
    companion object{
        lateinit var storage: FirebaseStorage
        var dialog: Dialog? = null

        fun uploadPicture(profileImg: Uri, context: Context){
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            builder.setView(R.layout.progress_bar)
            dialog = builder.create()
            storage = Firebase.storage
            val filename = profileImg.pathSegments[profileImg.pathSegments.lastIndex]

            val storageRef = FirebaseStorage.getInstance().getReference("images/$filename")

            var file = profileImg
            val uploadTask = storageRef.putFile(file)
            setDialog(true)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                setDialog(false)
            }.addOnFailureListener() {
                Toast.makeText(context, "Image could not be uploaded", Toast.LENGTH_SHORT).show()
                setDialog(false)
            }
        }

        private fun setDialog(show: Boolean) {
            if (show) dialog?.show() else dialog?.dismiss()
        }
    }
}