package com.example.adapp.model

data class User(val username:String?=null,
                val email:String?=null,
                val password:String?=null,
                val phoneNumber:String?=null) {
}
data class Response(var user: User? = null, var exception: Exception? = null)