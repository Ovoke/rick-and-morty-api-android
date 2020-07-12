package com.urban.androidhomework.model

data class CharacterData(
        var id: Int,
        var name: String,
        var status: String,
        var species: String,
        var gender: String,
        var image: String,
        var created: String,
        var location: LocationData
)