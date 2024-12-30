package com.example.mingleapp.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mingleapp.Model.Users
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirebaseDatabase {

    private val db = Firebase.firestore

    private val _users = MutableLiveData(mutableListOf<Users>())
    val users: LiveData<MutableList<Users>> get() = _users

    init {

        addSnapShotListener()

    }

    private fun addSnapShotListener() {

        db.collection("users").addSnapshotListener { snapshot, error ->

//            Check if snapshot is not null, check if data has been retried from the database correctly
            if (snapshot != null) {

//                Creates an empty list where we are going to store the users
                val userList = mutableListOf<Users>()

//                Loop through the documents in the snapshot
                for (document in snapshot.documents) {

//                    Try to convert the document to actual User objects
                    val user = document.toObject(Users::class.java)

//                    if convert is successful, add the user to the list
                    if( user != null) {
                        user.uid = document.id // Put the document ID in the user object
                        userList.add(user) // Add the user to the list
                    }

                }

                _users.value = userList
            } else { "A error: ${error?.message} has occurred"}

        }

    }

    fun addUser(name: String, email: String, uid: String) {

        val user = Users(name, email)
        db.collection("users").document(uid).set(user).addOnSuccessListener {

            Log.d("!!!", "User added successfully")

        }.addOnFailureListener {

            Log.d("!!!", "User add failed")
        }


    }
}


