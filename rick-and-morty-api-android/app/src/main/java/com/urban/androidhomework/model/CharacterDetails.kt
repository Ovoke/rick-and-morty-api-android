package com.urban.androidhomework.model

data class CharacterDetails(
        var character: CharacterData,
        var location: Location
) {
    fun isDead(): Boolean{
        return character.status.equals("dead", true)
    }
}
