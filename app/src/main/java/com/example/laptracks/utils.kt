package com.example.laptracks

fun convertLongToString(timeStamp: Long): String{
  val min = "%02d".format((timeStamp/1000)/60)
  val sec = "%02d".format((timeStamp/1000)%60)
  return "$min:$sec"
}

fun formatResults(participantTimes: Map<String,List<Long>>): String {
  var workout = ""
  for((key,value ) in participantTimes){
    val times = value.map{
      time ->
      convertLongToString(time)
    }
    workout += key + times.joinToString(", ") + "\n"
  }
  return workout
}