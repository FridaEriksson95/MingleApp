package com.example.mingleapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mingleapp.Model.Messages
import com.example.mingleapp.Model.Users
import com.example.mingleapp.Repositories.DatabaseRepository
import com.example.mingleapp.Repositories.MessageRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MessageViewModel: ViewModel() {

    val messageRepository = MessageRepository()
    val firebaseRepository = DatabaseRepository()

    private val auth = Firebase.auth

    val users: LiveData<MutableList<Users>> = firebaseRepository.users

    private val _messages = MutableLiveData<List<Messages>>()
    val messages: LiveData<List<Messages>> get() = _messages

    fun sendMessage(text: String, chatId: String) {
        messageRepository.sendMessage(text, chatId)
    }

    fun listenForMessages(chatId: String) {
        messageRepository.messageSnapShotListener(chatId, onSuccess = { fetch ->
            _messages.value = fetch

        }, onFailure = { e ->
            Log.d("MessageViewModel", "Error: ${e.message}")
        })
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
    fun deleteMessage(messageId: String, chatId: String) {
        messageRepository.deleteMessage(messageId,chatId)
    }

    fun updateMessage(messageId: String, chatId: String, newText: String) {
        messageRepository.updateMessage(messageId, chatId, newText)
    }

    fun generateChatId(otherUserId: String): String {
        val currentUserId = getCurrentUserId()
        return if (currentUserId > otherUserId) {
            "$currentUserId-$otherUserId"
        } else {
            "$otherUserId-$currentUserId"
        }
    }

}