package com.network

import android.util.Log
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

sealed class ResponseWrapper<T> {

    data class Success<T>(val body: T) : ResponseWrapper<T>()

    class Empty<T> : ResponseWrapper<T>()

    sealed class Error<T> : ResponseWrapper<T>() {
        data class BadRequest<T>(val errorCode: Int) : Error<T>()
        data class Forbidden<T>(val errorCode: Int) : Error<T>()
        data class NoInternetConnection<T>(val errorCode: Int) : Error<T>()
        data class Unknown<T>(val errorCode: Int) : Error<T>()
    }
}

suspend fun <T> getResult(call: suspend () -> Response<T>): ResponseWrapper<T> {
    try {
        val response = call()
        return if (response.isSuccessful) {
            when (val body = response.body()) {
                null -> ResponseWrapper.Empty()
                else -> ResponseWrapper.Success(body)
            }
        } else {
            Log.e(ResponseWrapper::class.simpleName, "Failed to process ${response.code()}")
            when (response.code()) {
                400 -> ResponseWrapper.Error.BadRequest(400)
                403 -> ResponseWrapper.Error.Forbidden(403)
                else -> {
                    ResponseWrapper.Error.Unknown(response.code())
                }
            }
        }
    } catch (e: Exception) {
        Log.e(ResponseWrapper::class.simpleName, "Failed to process request", e)
        return when (e) {
            is UnknownHostException, is IOException -> ResponseWrapper.Error.NoInternetConnection(
                601
            )
            else -> ResponseWrapper.Error.Unknown(0)
        }
    }
}