package tn.smartcoworking.data.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val sharedPreferences = context.applicationContext
        .getSharedPreferences("smart_coworking_prefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = sharedPreferences.getString("jwt_token", null)

        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            request.header("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
