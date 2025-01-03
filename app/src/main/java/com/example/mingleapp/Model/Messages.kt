package com.example.mingleapp.Model

data class Messages(val text: String,
                    val senderId: String,
                    var senderName: String,
                    val timestamp: Long = System.currentTimeMillis()) {

    constructor() : this("", "", "", 0)

}