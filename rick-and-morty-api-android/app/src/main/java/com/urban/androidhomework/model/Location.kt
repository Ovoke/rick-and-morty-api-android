package com.urban.androidhomework.model

data class Location(
        val id: Int,
        val name: String,
        var type: String,
        var dimension: String,
        var residents: ArrayList<String>,
        var url: String,
        var created: String
)