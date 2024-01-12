package com.example.laptracks

import androidx.lifecycle.ViewModel
import com.example.laptracks.data.LapTrackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LapTrackViewModel: ViewModel() {
  private val _uiState =  MutableStateFlow(LapTrackState())
  val uiState: StateFlow<LapTrackState> = _uiState.asStateFlow()

  fun setParticipants(participant: String){
    _uiState.update {
      currentState ->

      val newParticipants = if(currentState.participants.contains(participant)){
          currentState.participants.filter { it != participant }
        }else{
          currentState.participants + listOf(participant)
        }
      currentState.copy(participants = newParticipants, participantTimes = newParticipants.associateWith { emptyList<Long>() })
    }
  }

  fun setInterval(interval: String){
    _uiState.update {
      currentState ->
      currentState.copy(interval = interval)
    }
  }

  fun setParticipantTime(participant: String, timeStamp: Long){
    _uiState.update {
      currentState ->
      val newMap = currentState.participantTimes.keys.associateWith {
        key ->
          currentState.participantTimes.getValue(key) + if (key == participant) {
            listOf(timeStamp)
          } else {
            emptyList()
          }
      }
      currentState.copy(participantTimes = newMap)
    }
  }

  fun resetWorkout(){
    _uiState.value = LapTrackState()
  }
}