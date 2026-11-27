package tn.smartcoworking.data.model

data class RegisterResponse(
    val id: String,
    val nom: String,
    val prenom: String,
    val username: String,
    val email: String,
    val telephone: String,
    val role: Role,
    val profession: String?,
    val societe: String?
)