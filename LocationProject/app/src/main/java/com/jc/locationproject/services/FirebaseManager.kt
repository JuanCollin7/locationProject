package com.jc.locationproject.services

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jc.locationproject.database.LocationLog
import com.jc.locationproject.models.User
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsKey
import com.jc.locationproject.services.SharedPrefsManager.SharedPrefsManager


class FirebaseManager(context: Context) {

    private var firebaseDb: DatabaseReference = Firebase.database.reference
    private var sharedPrefsManager = SharedPrefsManager(context)

    private val userId by lazy { sharedPrefsManager.getIntValue(SharedPrefsKey.USER_ID) }

    fun uploadLocations(logs: List<LocationLog>) {

        val update: MutableMap<String, Any> = HashMap()
        logs.forEach {
            update["locations/${userId}/${it.uid}"] = it
        }

        getUserCurrentDisplacement { userDisplacement, _ ->
            val totalDisplacement = logs.sumOf { it.displacement ?: 0.0 } + userDisplacement

            logs.sortedByDescending { it.timestamp }.first()?.let {
                update["users/${userId}/lastTimestamp"] = it.timestamp ?: 0.0
                update["users/${userId}/lastLat"] = it.lat ?: 0.0
                update["users/${userId}/lastLon"] = it.lon ?: 0.0
                update["users/${userId}/displacement"] = totalDisplacement
            }

            firebaseDb.updateChildren(update).addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) Log.v("FIREBASE", "Success!") else Log.v("FIREBASE", "Failure")
            })
        }
    }

    private fun getUserCurrentDisplacement(onResult: (userDisplacement: Double, error: String?) -> Unit) {
        firebaseDb.child("users/${userId}/displacement").get().addOnSuccessListener { dataSnapshot ->
            onResult(dataSnapshot.value.toString().toDouble() ?: 0.0, null)
        }.addOnFailureListener{
            onResult(0.0, it.message)
        }
    }

    fun getUsers(onResult: (users: List<User>, error: String?) -> Unit) {
        val users: MutableList<User> = mutableListOf<User>()
        firebaseDb.child("users").get().addOnSuccessListener { dataSnapshot ->
            for (postSnapshot in dataSnapshot.children) {
                postSnapshot.getValue(User::class.java)?.let {
                    users.add(it)
                }
            }
            onResult(users, null)
        }.addOnFailureListener{
            onResult(emptyList(), it.message)
        }
    }

    fun getUserLocations(userId: Int, onResult: (locations: List<LocationLog>, error: String?) -> Unit) {
        val locations: MutableList<LocationLog> = mutableListOf<LocationLog>()
        firebaseDb.child("locations/${userId}").get().addOnSuccessListener { dataSnapshot ->
            for (postSnapshot in dataSnapshot.children) {
                postSnapshot.getValue(LocationLog::class.java)?.let {
                    locations.add(it)
                }
            }
            onResult(locations, null)
        }.addOnFailureListener{
            onResult(emptyList(), it.message)
        }
    }
}