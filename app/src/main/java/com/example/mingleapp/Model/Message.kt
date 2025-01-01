package com.example.mingleapp.Model

data class Message(val text: String, val senderId: String, val timestamp: Long = System.currentTimeMillis()) {

    constructor() : this("", "", 0)

}