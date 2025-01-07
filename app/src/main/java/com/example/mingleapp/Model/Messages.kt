package com.example.mingleapp.Model

import java.util.UUID

data class Messages(val text: String,
                    val senderId: String,
                    var senderName: String,
                    val timestamp: Long = System.currentTimeMillis()){
                    var messageId : String? = null

    constructor() : this("", "", "", 0)

}