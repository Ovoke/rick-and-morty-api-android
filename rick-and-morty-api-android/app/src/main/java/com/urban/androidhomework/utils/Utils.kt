package com.urban.androidhomework.utils

import com.urban.androidhomework.model.CharacterData
import retrofit2.Response

class Utils {
    companion object {
        fun getName(characterResponse: Response<List<CharacterData>>): List<String> {
            val names = mutableListOf<String>()
            for (ch in characterResponse.body()!!) {
                names.add(ch.name)
            }

            return names
        }

        fun getIDFromUrl(string: String): Int{
            val result = string.filter { it.isDigit() }
            return result.toInt()
        }

        fun getArrayFromArrayList(strings: ArrayList<String>): Array<String>{
            return strings.toTypedArray()
        }

        fun getIDsFromArrayUrls(strings: Array<String>): ArrayList<Int>{
            val result = strings.map { getIDFromUrl(it) }
            return result.toTypedArray().toCollection(ArrayList())
        }
    }
}