package com.example.myapplication.Projects.Instagram.RoodDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toList(json: String?): List<Int> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(json, type)
    }


}
