package tn.smartcoworking.data.model

data class EspaceRequest(
    val nom: String,
    val description: String,
    val ville: String,
    val adresse: String,
    val capacite: Int,
    val prixHeure: Double,
    val type: TypeEspace
)