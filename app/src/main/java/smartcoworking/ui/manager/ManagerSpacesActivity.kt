package tn.smartcoworking.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.R
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.Espace
import tn.smartcoworking.data.repository.EspaceRepository

class ManagerSpacesActivity : AppCompatActivity() {

    private lateinit var rvManagerEspaces: RecyclerView
    private lateinit var btnAddEspace: Button

    private lateinit var adapter: ManagerSpacesAdapter
    private lateinit var repository: EspaceRepository

    private val espaces = mutableListOf<Espace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_spaces)

        ApiClient.init(this)

        repository = EspaceRepository()

        rvManagerEspaces = findViewById(R.id.rvManagerEspaces)
        btnAddEspace = findViewById(R.id.btnAddEspace)

        setupRecyclerView()
        setupButtons()

        loadMySpaces()
    }

    // Configure RecyclerView
    private fun setupRecyclerView() {

        adapter = ManagerSpacesAdapter(
            espaces,
            onModifierClick = { espace ->
                openEditForm(espace)
            },
            onSupprimerClick = { espace ->
                confirmDelete(espace)
            }
        )

        rvManagerEspaces.layoutManager = LinearLayoutManager(this)
        rvManagerEspaces.adapter = adapter
    }

    // Add button
    private fun setupButtons() {

        btnAddEspace.setOnClickListener {
            openAddForm()
        }
    }

    // Load manager's spaces from backend
    private fun loadMySpaces() {

        repository.getMySpaces(object : Callback<List<Espace>> {

            override fun onResponse(
                call: Call<List<Espace>>,
                response: Response<List<Espace>>
            ) {

                if (response.isSuccessful) {

                    val result = response.body()

                    if (result != null) {
                        adapter.updateData(result.filter { it.actif })
                    }

                } else {
                    Toast.makeText(
                        this@ManagerSpacesActivity,
                        "Erreur serveur : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Espace>>, t: Throwable) {

                Toast.makeText(
                    this@ManagerSpacesActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    // Open empty form to create a space
    private fun openAddForm() {

        val intent = Intent(this, EspaceFormActivity::class.java)
        startActivity(intent)
    }

    // Open form with existing space information
    private fun openEditForm(espace: Espace) {

        val intent = Intent(this, EspaceFormActivity::class.java)

        intent.putExtra("espace_id", espace.id)

        startActivity(intent)
    }

    // Ask for confirmation before deleting
    private fun confirmDelete(espace: Espace) {

        AlertDialog.Builder(this)
            .setTitle("Supprimer cet espace")
            .setMessage("Voulez-vous vraiment supprimer ${espace.nom} ?")
            .setPositiveButton("Supprimer") { _, _ ->
                deleteEspace(espace.id)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    // Delete space through backend
    private fun deleteEspace(id: Long) {

        repository.deleteEspace(id, object : Callback<Void> {

            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@ManagerSpacesActivity,
                        "Espace supprimé avec succès",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadMySpaces()

                } else {

                    Toast.makeText(
                        this@ManagerSpacesActivity,
                        "Erreur lors de la suppression : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                Toast.makeText(
                    this@ManagerSpacesActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    // Refresh list when returning from form
    override fun onResume() {
        super.onResume()

        if (::repository.isInitialized) {
            loadMySpaces()
        }
    }
}
