package com.example.laptracks.data

data class LapTrackState(
  val participants: List<String> = listOf(),
  val interval: String = "Intervals",
  val participantTimes: Map<String, List<Long>> = mapOf()
)
