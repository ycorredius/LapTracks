package com.shaunyarbrough.laptracks.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
	@POST("sign_in")
	suspend fun loginUser(@Body body: Auth): Response<JWT>
}