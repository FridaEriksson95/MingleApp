package com.example.mingleapp.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mingleapp.Model.Users
import com.example.mingleapp.Repositories.DatabaseRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseViewModel : ViewModel() {
    val db = DatabaseRepository()

    val users : LiveData<MutableList<Users>> get() = db.users
    val favoriteUsers: LiveData<MutableList<Users>> = db.favoriteUsers

    fun addUser(userName: String, email: String, uid: String, birth: String,imageResourceId : Int, isFavorite: Boolean) {
        db.addUser(userName,email,uid,birth,imageResourceId,isFavorite)
    }

    fun onQueryTextChange(query : String) {
        db.onQueryTextChange(query)
    }

    fun updateFavoriteStatus(user: Users) {
        db.updateUserFavoriteStatus(user, user.isFavorite)
    }
}