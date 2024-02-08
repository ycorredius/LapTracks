package com.shaunyarbrough.laptracks

import com.shaunyarbrough.laptracks.data.Student

fun convertLongToString(timeStamp: Long): String{
  val min = "%02d".format((timeStamp/1000)/60)
  val sec = "%02d".format((timeStamp/1000)%60)
  return "$min:$sec"
}

fun formatResults(participantTimes: Map<Student,List<Long>>): String {
  var workout = ""
  for((key,value ) in participantTimes){
    val times = value.map{
      time ->
      convertLongToString(time)
    }
    workout += key.displayName + times.joinToString(", ") + "\n"
  }
  return workout
}

fun getLapTimeAverage(laps: List<Long>): String{
  var result = 0L

  laps.forEach {
    result += it
  }

  result /= laps.size
  return convertLongToString(result)
}

fun getLapTimeString(laps: List<Long>): String{
  return if(laps.isEmpty()) {
    "0"
  } else if(laps.size <= 1){
    convertLongToString(laps[0])
  }else {
    convertLongToString(laps[laps.size - 1] - laps[laps.size - 2])
  }
}

fun getLapTimeLong(laps: List<Long>, totalTime: Long): Long{
  return if(laps.isEmpty() || laps.size <= 1){
    totalTime
  }else {
    totalTime - laps[laps.size - 1]
  }
}