package com.example.mingleapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mingleapp.Model.Users
import com.example.mingleapp.Repositories.DatabaseRepository

class FirebaseViewModel : ViewModel() {
    val db = DatabaseRepository()

    val users : LiveData<MutableList<Users>> get() = db.users

    fun onQueryTextChange(query : String) {
        db.onQueryTextChange(query)
    }

    fun updateUsername(uid: String, newUserName: String) {
        db.updateUsername(uid, newUserName)
    }
}