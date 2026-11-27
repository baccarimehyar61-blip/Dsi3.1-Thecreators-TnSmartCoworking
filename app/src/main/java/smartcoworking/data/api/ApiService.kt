package tn.smartcoworking.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import tn.smartcoworking.data.model.Espace
import tn.smartcoworking.data.model.EspaceRequest
import tn.smartcoworking.data.model.LoginRequest
import tn.smartcoworking.data.model.LoginResponse
import tn.smartcoworking.data.model.RegisterRequest
import tn.smartcoworking.data.model.RegisterResponse

interface ApiService {

    // =========================
    // AUTHENTICATION
    // =========================

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>


    // =========================
    // MEMBER - SPACES
    // =========================

    @GET("espaces")
    fun getEspaces(): Call<List<Espace>>


    @GET("espaces/{id}")
    fun getEspaceById(@Path("id") id: Long): Call<Espace>


    // =========================
    // MANAGER - MY SPACES
    // =========================

    @GET("espaces/mes-espaces")
    fun getMySpaces(): Call<List<Espace>>


    // =========================
    // MANAGER - CRUD
    // =========================

    @POST("espaces")
    fun createEspace(@Body request: EspaceRequest): Call<Espace>

    @PUT("espaces/{id}")
    fun updateEspace(@Path("id") id: Long, @Body request: EspaceRequest): Call<Espace>

    @DELETE("espaces/{id}")
    fun deleteEspace(@Path("id") id: Long): Call<Void>

}
