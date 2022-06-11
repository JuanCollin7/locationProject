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
            update["locations/" + userId + "/" + it.uid] = it
        }

        firebaseDb.updateChildren(update).addOnCompleteListener(OnCompleteListener<Void?> { task ->
            if (task.isSuccessful) Log.v("FIREBASE", "Success!") else Log.v("FIREBASE", "Failure")
        })
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
}