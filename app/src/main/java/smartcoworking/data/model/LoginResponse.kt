package tn.smartcoworking.data.model

data class LoginResponse(
    val token: String,
    val id: String,
    val nom: String,
    val prenom: String,
    val email: String,
    val role: Role
)