package com.shaunyarbrough.laptracks.service

import com.shaunyarbrough.laptracks.data.User
import kotlinx.coroutines.flow.Flow


interface AccountService {
	val currentUser: Flow<User?>

	val currentUserId: String
	fun hasUser(): Boolean
	suspend fun signIn(email: String, password: String)
	suspend fun signUp(email: String, password: String)

	suspend fun anonymousSignIn()
	suspend fun signOut()
	suspend fun deleteAccount()
}