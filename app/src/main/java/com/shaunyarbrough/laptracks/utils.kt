package com.shaunyarbrough.laptracks

import com.shaunyarbrough.laptracks.data.Student

fun convertLongToString(timeStamp: Long): String {
	val milliSec = "%02d".format((timeStamp % (60 * 1000)) / 1000)
	val sec = "%02d".format(timeStamp / (60 * 1000) % 60)
	val min = "%02d".format((timeStamp / (60 * 1000)) / 60)

	return "${min}:${sec}.${milliSec}"
}

fun formatResults(participantTimes: Map<Student, List<Long>>): String {
	var workout = ""
	for ((key, value) in participantTimes) {
		val times = value.map { time ->
			convertLongToString(time)
		}
		workout += key.displayName + " " + times.joinToString(", ") + "\n"
	}
	return workout
}

fun getLapTimeAverage(laps: List<Long>): String {
	var result = 0L
	return if (laps.isEmpty()) {
		"00:00.00"
	} else {
		laps.forEachIndexed { index, value ->
			result += if (index == 0) {
				value
			} else {
				(value - laps[index - 1])
			}
		}
		result /= laps.size
		convertLongToString(result)
	}
}

fun getFastestLap(laps: List<Long>): String {
	if (laps.isEmpty()) return "00:00.00"

	var fastestLap: Long = 5000000000_00L
	laps.forEachIndexed { index, l ->
		if (index == 0) {
			fastestLap = l
		} else {
			val time = l - laps[index - 1]
			if (time < fastestLap) {
				fastestLap = time
			}
		}
	}
	return convertLongToString(fastestLap)
}

fun getSlowestLap(laps: List<Long>): String {
	if (laps.isEmpty()) return "00:00.00"

	var slowest: Long = 0L
	laps.forEachIndexed { index, l ->
		if (index == 0) {
			slowest = l
		} else {
			val time = l - laps[index - 1]
			if (time > slowest) slowest = time
		}
	}
	return convertLongToString(slowest)
}

fun getLastLapTimeString(laps: List<Long>): String {
	val previousLap = laps.size - 2
	val lastLap = laps.size - 1
	return if (laps.isEmpty()) {
		"0"
	} else if (laps.size == 1) {
		convertLongToString(laps[lastLap])
	} else {
		convertLongToString(laps[lastLap] - laps[previousLap])
	}
}