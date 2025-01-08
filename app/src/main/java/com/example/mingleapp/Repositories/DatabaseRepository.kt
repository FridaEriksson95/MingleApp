package com.example.mingleapp.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mingleapp.Model.Users
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class DatabaseRepository {

    private val db = Firebase.firestore
    private val _users = MutableLiveData(mutableListOf<Users>())
    private val _favoriteUsers = MutableLiveData(mutableListOf<Users>())

    val users: LiveData<MutableList<Users>> get() = _users
    val favoriteUsers: LiveData<MutableList<Users>> get() =_favoriteUsers


    init {
        addSnapShotListener()
    }

    private fun addSnapShotListener() {
        db.collection("users").addSnapshotListener { snapshot, error ->

            if (snapshot != null) {
                val userList = mutableListOf<Users>()
                val favoriteList = mutableListOf<Users>()
                for (document in snapshot.documents) {
                    val user = document.toObject(Users::class.java)
                    if (user != null) {
                        user.uid = document.id
                        userList.add(user)
                        if (user.isFavorite) {
                            favoriteList.add(user)
                        }
                    }
                }
                _users.postValue(userList)
                _favoriteUsers.postValue(favoriteList)
            } else {
                Log.e("DatabaseRepo", "Error: ${error?.message}")
            }
        }
    }

        fun addUser(
            userName: String,
            email: String,
            uid: String,
            birth: String,
            imageResourceid: Int,
            isFavorite: Boolean
        ) {
            val user = Users(userName, email, uid, birth, imageResourceid, isFavorite)
            db.collection("users").document(uid).set(user).addOnSuccessListener {
                Log.d("FireBaseDatabase", "User added successfully")

            }.addOnFailureListener {
                Log.d("FireBaseDatabase", "User add failed", it)
            }
        }

        fun onQueryTextChange(query: String) {
            db.collection("users")
                .whereGreaterThanOrEqualTo("userName", query)
                .whereLessThanOrEqualTo("userName", query + '\uf8ff')
                .addSnapshotListener { snapshot, error ->
                    if (snapshot != null) {
                        val userList = mutableListOf<Users>()
                        for (doc in snapshot.documents) {
                            val user = doc.toObject(Users::class.java)
                            if (user != null) {
                                user.uid = doc.id
                                userList.add(user)
                            }
                        }
                        _users.value = userList
                    } else {
                        Log.d("DatabaseRepo", "Error")
                    }
                }
        }

            fun updateUserFavoriteStatus(user: Users, isFavorite: Boolean) {
                db.collection("users").document(user.uid ?: "").update("isFavorite", isFavorite)
                    .addOnSuccessListener {
                        Log.d("DatabaseRepo", "favorite status updated")
                    }.addOnFailureListener {
                        Log.e("DatabaseRepo", "favorite status failed to update", it)
                    }
            }
        }