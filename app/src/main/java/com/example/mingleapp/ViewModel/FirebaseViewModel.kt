package com.example.mingleapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mingleapp.Model.Users
import com.example.mingleapp.Repositories.DatabaseRepository

class FirebaseViewModel : ViewModel() {
    val db = DatabaseRepository()

    val users : LiveData<MutableList<Users>> get() = db.users

    fun addUser(userName: String, email: String, uid: String, birth: String,imageResourceId : Int) {
        db.addUser(userName,email,uid,birth,imageResourceId)
    }

    fun onQueryTextChange(query : String) {
        db.onQueryTextChange(query)
    }

}