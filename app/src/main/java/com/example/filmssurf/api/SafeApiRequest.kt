package com.example.filmssurf.api

import com.example.filmssurf.other.*
import com.example.filmssurf.other.Utils.SUCCESS_200
import com.example.filmssurf.other.Utils.UNSUCCESS_401
import com.example.filmssurf.other.Utils.UNSUCCESS_422
import com.example.filmssurf.other.Utils.UNSUCCESS_429
import com.example.filmssurf.other.Utils.UNSUCCESS_500
import com.example.filmssurf.other.Utils.UNSUCCESS_503
import com.example.filmssurf.other.Utils.UNSUCCESS_504
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T> handleApiRequest(call: suspend () -> Response<T>): T? {
        val response = call.invoke()

        return if(response.isSuccessful){
            when(response.code()){
                SUCCESS_200 -> response.body()!!
                else -> null
            }
        } else {
            when(response.code()){
                UNSUCCESS_429 -> throw ApiLimitException("API limit reached.\nPlease try again later.")
                UNSUCCESS_422 -> throw InvalidFormatException("Your request parameters are incorrect.")
                UNSUCCESS_401 -> throw InvalidApiKeyException("Access to your account has been suspended, contact TMDb.")
                UNSUCCESS_503 -> throw ServiceOfflineException("Access to your account has been suspended, contact TMDb.")
                UNSUCCESS_500 -> throw InternalErrorException("Access to your account has been suspended, contact TMDb.")
                UNSUCCESS_504 -> throw TimeoutRequestException("Your request to the backend server timed out. Try again.")
                // and other ...
                else -> null
            }
        }
    }
}