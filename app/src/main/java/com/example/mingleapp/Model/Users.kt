package com.example.mingleapp.Model

data class Users(var userName: String? = null,
                 var email: String? = null,
                 var uid: String? = null,
                 var dateOfBirth: String? = null,
                 val imageResourceID : Int,
                 var isFavorite : Boolean) {

    constructor() : this("", "", "", "",0, false)
}