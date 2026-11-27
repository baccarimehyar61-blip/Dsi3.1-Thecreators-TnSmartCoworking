package tn.smartcoworking.data.model

data class Espace(
    val id: Long,
    val nom: String,
    val description: String,
    val ville: String,
    val adresse: String,
    val capacite: Int?,
    val prixHeure: Double,
    val type: TypeEspace,
    val actif: Boolean,
    val dateCreation: String?,
    val nomGestionnaire: String?
)