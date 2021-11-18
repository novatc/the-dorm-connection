package Firestore

import Constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.activities.activities.SignInActivity
import com.novatc.ap_app.activities.activities.SignUpActivity
import model.Event
import model.Room
import model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await


class Fireclass {

    private val mFirestore = Firebase.firestore

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getCurrentUser(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val name = user.email
        return name
    }

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFirestore.collection(Constants.USER).document(getCurrentUserID())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error in Firestore (Register) ", e)
            }
    }

    fun signInUser(activity: SignInActivity) {
        mFirestore.collection(Constants.USER).document(getCurrentUserID()).get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                activity.signInSuccess(loggedInUser!!)
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error in Firestore (LogIn) ", e)
            }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun deleteUser() {
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("PROFILE", "User account deleted.")
                }
            }
    }

    fun updateUserName(name: String) {
        val user = Firebase.auth.currentUser
        val updates = userProfileChangeRequest {
            displayName = name
        }
        user!!.updateProfile(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PROFILE", "User profile updated.")
            }

        }
    }

    suspend fun getUserData(uid: String): User? {
        val snapshot = mFirestore.collection(Constants.USER).document(uid).get().await()
        val user = snapshot.toObject<User>()
        Log.e("FIRE", user!!.username)
        return  user
    }

    fun addEventToDD(event: Event) {
        mFirestore.collection(Constants.EVENTS).document().set(event, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("EVENT", "Event saved to DB")
            }.addOnFailureListener { e ->
            Log.e("EVENT", "Error while saving: $e")
        }
    }

    fun addRoomToDD(room: Room) {
        mFirestore.collection(Constants.ROOMS).document().set(room, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("EVENT", "Event saved to DB")
            }.addOnFailureListener { e ->
            Log.e("EVENT", "Error while saving: $e")
        }
    }
}
