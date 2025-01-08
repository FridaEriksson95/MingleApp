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
                        message.messageId = document.id
                        messages.add(message)
                    }
                }
                onSuccess(messages)
            } else {
                onFailure(Exception("No messages found ${error?.message}"))
            }
        }
    }
    fun deleteMessage(messageId : String,chatId: String) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .delete()
            .addOnSuccessListener {
                Log.d("MessageRepo","Successfully deleted $messageId")
            }
            .addOnFailureListener {
                Log.d("MessageRepo","Failed to delete $messageId")
            }
    }

    fun updateMessage(messageId: String, chatId: String, newText: String) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .update("text", newText)
            .addOnSuccessListener {
                Log.d("MessageRepository", "Message updated successfully")
            }
            .addOnFailureListener {
                Log.d("MessageRepository", "Message update failed")
            }
    }
}