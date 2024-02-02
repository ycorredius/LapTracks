package com.example.laptracks.data

import androidx.room.TypeConverter

class Converters {
  @TypeConverter
  fun fromListToString(lapList: List<Long>): String{
    return lapList.joinToString(",")
  }

  @TypeConverter
  fun fromStringToList(lapList: String): List<Long>{
    return lapList.split(",").map { it.toLong() }
  }
}