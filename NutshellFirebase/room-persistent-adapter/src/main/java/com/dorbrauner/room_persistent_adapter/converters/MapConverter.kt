package com.dorbrauner.room_persistent_adapter.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


class MapConverter  {

    @TypeConverter
    fun toJson(map: Map<String, String>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toMap(json: String): Map<String, String> {
        val typeOfHashMap = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(json, typeOfHashMap)
    }
}