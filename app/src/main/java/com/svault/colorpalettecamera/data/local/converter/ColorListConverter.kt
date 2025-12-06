package com.svault.colorpalettecamera.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.svault.colorpalettecamera.data.model.ColorInfo

class ColorListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromColorInfoList(value: List<ColorInfo>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toColorInfoList(value: String): List<ColorInfo>? {
        val listType = object : TypeToken<List<ColorInfo>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromColorInfo(value: ColorInfo?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toColorInfo(value: String?): ColorInfo? {
        return value?.let { gson.fromJson(it, ColorInfo::class.java) }
    }
}
