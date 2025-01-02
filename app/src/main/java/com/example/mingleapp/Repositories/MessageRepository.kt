package com.example.mingleapp.Repositories


import android.util.Log
import com.example.mingleapp.Model.Messages
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MessageRepository {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun sendMessage(text: String, chatId: String) {
        val currentUser = auth.currentUser
        val uid = currentUser?.uid ?: ""
        val message = Messages(text, uid, System.currentTimeMillis().toString())

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d("MessageRepository", "Message sent successfully")
            }
            .addOnFailureListener {
                Log.d("MessageRepository", "Message sent failed")
            }
    }

    fun messageSnapShotListener(chatId: String, onSuccess: (List<Messages>) -> Unit, onFailure: (Exception) -> Unit) {

        db.collection("chats").document(chatId).collection("messages").orderBy("timestamp").addSnapshotListener { snapshot, error ->
            val messages = mutableListOf<Messages>()
            if (snapshot != null && !snapshot.isEmpty) {

                for (document in snapshot.documents) {
                    val message = document.toObject(Messages::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                onSuccess(messages)
            } else {
                onFailure(Exception("No messages found ${error?.message}"))
            }
        }
    }
}