package com.valoy.dragonball.network

import com.valoy.dragonball.dto.CharacterDTO
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class CharactersService(private val client: OkHttpClient = OkHttpClient()) {

    fun execute(callback: (resource: Resource<List<CharacterDTO>>) -> Unit) {
        client.newCall(createRequest()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(Resource.Error("Characters error"))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        response.body?.let {
                            callback.invoke(Resource.Success(CharacterDTO.listFromJSON(it.string())))
                        }
                    }
                }
            }
        })
    }

    private fun createRequest(): Request {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body: RequestBody = "{\n    \"name\" : \"\"\n}".toRequestBody(mediaType)

        return Request.Builder()
            .url("https://dragonball.keepcoding.education/api/heros/all")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Bearer eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJlbWFpbCI6InZpYy5hbG95QGdtYWlsLmNvbSIsImV4cGlyYXRpb24iOjY0MDkyMjExMjAwLCJpZGVudGlmeSI6IkQ4MUQwQkMwLTk3QjUtNDZFQS1BRDYyLUJBNkVDQ0U4QzhGMyJ9.qV1jImQMi7nELOaWkgILHzw4qm5taEOWMzTDR4uCcNM"
            )
            .addHeader("Content-Type", "application/json")
            .build()
    }

}