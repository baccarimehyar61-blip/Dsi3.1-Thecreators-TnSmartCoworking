package tn.smartcoworking.data.repository

import retrofit2.Callback
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.Espace
import tn.smartcoworking.data.model.EspaceRequest

class EspaceRepository {

    private val apiService = ApiClient.apiService

    // Get all available spaces (MEMBER)
    fun getEspaces(callback: Callback<List<Espace>>) {
        apiService.getEspaces().enqueue(callback)
    }

    // Get details of one space
    fun getEspaceById(
        id: Long,
        callback: Callback<Espace>
    ) {
        apiService.getEspaceById(id).enqueue(callback)
    }

    // Get spaces belonging to the connected manager
    fun getMySpaces(callback: Callback<List<Espace>>) {
        apiService.getMySpaces().enqueue(callback)
    }

    // Add a new space (MANAGER)
    fun createEspace(
        request: EspaceRequest,
        callback: Callback<Espace>
    ) {
        apiService.createEspace(request).enqueue(callback)
    }

    // Modify an existing space (MANAGER)
    fun updateEspace(
        id: Long,
        request: EspaceRequest,
        callback: Callback<Espace>
    ) {
        apiService.updateEspace(id, request).enqueue(callback)
    }

    // Delete a space (MANAGER)
    fun deleteEspace(
        id: Long,
        callback: Callback<Void>
    ) {
        apiService.deleteEspace(id).enqueue(callback)
    }
}
