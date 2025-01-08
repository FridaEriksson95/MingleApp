package com.example.mingleapp.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

class AuthRepository {

    private val auth = Firebase.auth
    private val db = DatabaseRepository()

    fun createAccount(birth: String, userName: String, email: String, password: String,imageResourceID: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { isCreated ->
            if (isCreated.isSuccessful) {

                val currentUser = auth.currentUser
                val uid = currentUser?.uid ?: ""
                db.addUser(userName, email, uid,birth, imageResourceID)

                onSuccess()
            } else {
                val errorMessage = when (val error = isCreated.exception) {
                    is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email address format."
                    is FirebaseAuthUserCollisionException -> "Email is already registered."
                    else -> error?.localizedMessage ?: "Unknown error occurred."
                }
                onFailure(Exception(errorMessage))
            }
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {

                    Log.d("FireBaseAuth", "User ${auth.currentUser?.email} signed in successfully")
                    onSuccess()
                }
            } else {
                val errorMessage = when (val error = task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
                    is FirebaseAuthUserCollisionException -> "This email is already registered"
                    else -> error?.localizedMessage ?: "Sign in failed. Please try again"
                }
                Log.d("FireBaseAuth", "User sign in failed ${task.exception?.message}")
                onFailure(Exception(errorMessage))
            }
        }
    }

    fun logOut() : LiveData<Boolean>{
        val logOut = MutableLiveData<Boolean>()

        auth.signOut()
        logOut.postValue(true)
        return logOut
    }
    
    fun getCurrentUserId() : String {
        return auth.currentUser?.uid ?: ""
    }

    fun deleteAccount(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.deleteUser(getCurrentUserId(), onSuccess = {
            Log.d("DatabaseRepository", "User data deleted successfully")
        }, onFailure = { exception ->
            Log.d("DatabaseRepository", "User deletion failed: ${exception.message}")
        })
        auth.currentUser?.delete()?.addOnSuccessListener {
            onSuccess()
        }
            ?.addOnFailureListener {
                exception -> onFailure(exception)
            }
    }
}