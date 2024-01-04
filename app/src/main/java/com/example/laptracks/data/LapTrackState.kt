package com.example.laptracks.data

data class LapTrackState(
  val participants: List<String> = listOf(),
  val interval: String = "",
  val participantTimes: Map<String, List<String>> = mapOf()
)
