package com.example.myapplication.dto

import java.io.Serializable

class Massage(
    login: String,
    content: String
): Serializable{
    var login:String = login
    var date:String? = null
    var content:String = content
    var id:String? = null
}