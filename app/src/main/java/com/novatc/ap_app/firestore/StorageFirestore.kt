package com.novatc.ap_app.firestore

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.novatc.ap_app.constants.UploadDirectories
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageFirestore @Inject constructor() {

    private val storage = Firebase.storage

    suspend fun uploadImage(
        directory: UploadDirectories,
        id: String,
        uri: Uri
    ): UploadTask.TaskSnapshot? {
        val storageRef = storage.reference.child("images/${directory.directory}/${id}.png")
        return storageRef.putFile(uri).await()
    }

    suspend fun deleteImage(directory: UploadDirectories, id: String): Void? {
        val storageRef = storage.reference.child("images/${directory.directory}/${id}.png")
        return storageRef.delete().await()
    }

    suspend fun loadImage(
        directory: UploadDirectories,
        id: String
    ): String {
        val storageRef = storage.reference.child("images/${directory.directory}/${id}.png")
        val uri = storageRef.downloadUrl.await()
        return uri.toString()
    }
}
