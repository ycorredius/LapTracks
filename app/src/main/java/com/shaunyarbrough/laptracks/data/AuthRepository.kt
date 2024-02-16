package com.shaunyarbrough.laptracks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
	private val authService: AuthService,
	private val dataStore: DataStore<Preferences>
) {

	val accessTokenFlow: Flow<String> = dataStore.data
		.map { preferences ->
			preferences[ACCESS_TOKEN] ?: ""
		}
	suspend fun loginUser(email: String, password: String): Response<JWT> {
		val response = authService.loginUser(Auth(email, password))
		if(response.isSuccessful){
			response.body()?.token?.let { updateJWT(it) }
		}
		return response
	}

	private suspend fun updateJWT(jwt: String){
		dataStore.edit {
			preferences ->
			preferences[ACCESS_TOKEN] = jwt
		}
	}

	companion object{
		private val ACCESS_TOKEN = stringPreferencesKey("access_token")
	}
}