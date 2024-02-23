package com.shaunyarbrough.laptracks.service.impl

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shaunyarbrough.laptracks.LapTrackApplication
import com.shaunyarbrough.laptracks.data.User
import com.shaunyarbrough.laptracks.service.AccountService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor() : AccountService {
	override val currentUser: Flow<User?>
		get() = callbackFlow {
			val listener =
				FirebaseAuth.AuthStateListener { auth ->
					this.trySend(auth.currentUser?.let { User(it.uid) })
				}
			Firebase.auth.addAuthStateListener(listener)
			awaitClose{ Firebase.auth.removeAuthStateListener(listener)}
		}

	override val currentUserId: String
		get() = Firebase.auth.currentUser?.uid.orEmpty()

	override fun hasUser(): Boolean {
		return Firebase.auth.currentUser != null
	}

	override suspend fun signIn(email: String, password: String) {
		try{
			Firebase.auth.signInWithEmailAndPassword(email,password).await()
		} catch (e: Exception){
			handleAuthenticationError(e)
		}
	}

	override suspend fun signUp(email: String, password: String) {
		try {
			Firebase.auth.createUserWithEmailAndPassword(email,password).await()
		}catch (e: Exception){
			handleAuthenticationError(e)
		}
	}

	override suspend fun anonymousSignIn() {
		try {
			Firebase.auth.signInAnonymously().await()
		} catch (e: Exception){
			handleAuthenticationError(e)
		}
	}

	override suspend fun signOut() {
		Firebase.auth.signOut()
	}

	override suspend fun deleteAccount() {
		Firebase.auth.currentUser!!.delete().await()
	}

	private fun handleAuthenticationError(e: Exception){
		val errorMessage = when(e){
			is FirebaseAuthException -> e.localizedMessage ?: "Authentication failed"
			else -> "Authentication Failed"
		}
		Toast.makeText(LapTrackApplication.instance, errorMessage, Toast.LENGTH_SHORT).show()
	}
}