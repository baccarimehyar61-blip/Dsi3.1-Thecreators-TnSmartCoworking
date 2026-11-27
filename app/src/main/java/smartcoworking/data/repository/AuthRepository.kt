package tn.smartcoworking.data.repository

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.LoginRequest
import tn.smartcoworking.data.model.LoginResponse
import tn.smartcoworking.data.model.RegisterRequest
import tn.smartcoworking.data.model.RegisterResponse

class AuthRepository(context: Context) {

    private val apiService = ApiClient.apiService

    private val preferences = context.applicationContext
        .getSharedPreferences("smart_coworking_prefs", Context.MODE_PRIVATE)

    // LOGIN
    fun login(
        request: LoginRequest,
        callback: Callback<LoginResponse>
    ) {
        apiService.login(request).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        preferences.edit()
                            .putString("jwt_token", loginResponse.token)
                            .apply()
                    }
                }

                callback.onResponse(call, response)
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                callback.onFailure(call, t)
            }
        })
    }

    // REGISTER
    fun register(
        request: RegisterRequest,
        callback: Callback<RegisterResponse>
    ) {
        apiService.register(request).enqueue(callback)
    }

    // LOGOUT
    fun logout() {
        preferences.edit()
            .remove("jwt_token")
            .apply()
    }
}
