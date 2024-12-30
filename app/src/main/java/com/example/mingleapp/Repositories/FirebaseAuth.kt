package com.example.mingleapp.Repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

class FirebaseAuth {

    private val auth = Firebase.auth
    private val db = FirebaseDatabase()


    fun createAccounts(name: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { isCreated ->
            if (isCreated.isSuccessful) {

                val currentUser = auth.currentUser
                val uid = currentUser?.uid ?: ""

//                Add firebase cloud user, make method in FirebaseDatabase, put it here
                db.addUser(name, email, uid)

//                Log.d("!!!", "User created successfully")
                onSuccess()
            } else {
//                Log.d("!!!", "User creation failed ${isCreated.exception?.message}")

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



//Sign in
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {

//                    Make Toast, Intent, Callbacks(?)
                    Log.d("!!!", "User ${auth.currentUser?.email} signed in successfully")
                    onSuccess()

                } else {
                    auth.signOut() // Sign out if email is not verified, prevent user from logging in, Firebase allows logins without verification, must have this code, its an extra precaution control
                    onFailure(Exception("Email is not verified, please verify your email"))
                }

            } else {

                val errorMessage = when (val error = task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
                    is FirebaseAuthUserCollisionException -> "This email is already registered"
                    else -> error?.localizedMessage ?: "Sign in failed. Please try again"
                }

                Log.d("!!!", "User sign in failed ${task.exception?.message}")
                onFailure(Exception(errorMessage))
            }

        }

    }
}