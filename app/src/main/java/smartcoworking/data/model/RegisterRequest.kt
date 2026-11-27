package tn.smartcoworking.data.model

data class RegisterRequest(
    val nom: String,
    val prenom: String,
    val username: String,
    val email: String,
    val motDePasse: String,
    val telephone: String,
    val role: Role,
    val profession: String? = null,
    val societe: String? = null
)