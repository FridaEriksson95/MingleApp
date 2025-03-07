package com.example.mingleapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mingleapp.Repositories.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class AuthViewModel: ViewModel() {

    private val authRepository = AuthRepository()

    fun createAccount(birth: String, userName: String, email: String, password: String,imageResourceID : Int, isFavorite : Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        authRepository.createAccount(birth, userName, email, password,imageResourceID, isFavorite, onSuccess, onFailure)
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        authRepository.signIn(email, password, onSuccess, onFailure)
    }

    fun sendPasswordResetEmail(email: String): Task<Void> {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.sendPasswordResetEmail(email)
    }

    fun logOut () : LiveData<Boolean> {
       return authRepository.logOut()
    }
    
    fun getCurrentUserId() : String {
        return authRepository.getCurrentUserId()
    }

    fun deleteAccount(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        authRepository.deleteAccount(onSuccess, onFailure)
    }
}