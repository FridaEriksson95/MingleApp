package com.example.mingleapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mingleapp.Repositories.FirebaseAuth

class AuthViewModel: ViewModel() {

    private val authRepository = FirebaseAuth()

    fun createAccount(birth: String, userName: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        authRepository.createAccount(birth, userName, email, password, onSuccess, onFailure)
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        authRepository.signIn(email, password, onSuccess, onFailure)
    }

}