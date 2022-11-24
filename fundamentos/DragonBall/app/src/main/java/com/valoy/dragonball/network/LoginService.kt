package com.valoy.dragonball.network

import android.util.Base64
import com.valoy.dragonball.dto.UserDTO
import com.valoy.dragonball.presentation.login.LoginViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginService(private val client: OkHttpClient = OkHttpClient()) {

    fun execute(user: UserDTO, callback: (resource: Resource<String>) -> Unit) {
        val request = createRequest(user.user, user.password)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(Resource.Error("Login error"))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        callback.invoke(Resource.Success(response.body!!.string()))
                    }

                }
            }
        })
    }

    private fun createRequest(user: String, password: String): Request {
        val mediaType = "text/plain".toMediaTypeOrNull()
        val body: RequestBody = "".toRequestBody(mediaType)

        return Request.Builder()
            .url("https://dragonball.keepcoding.education/api/auth/login")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Basic " + Base64.encodeToString(("$user:$password").toByteArray(), Base64.NO_WRAP)
            )
            .build()
    }

}