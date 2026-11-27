package tn.smartcoworking.ui.spaces

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

class SpacesActivity : AppCompatActivity() {

    private lateinit var rvEspaces: RecyclerView
    private lateinit var spacesAdapter: SpacesAdapter
    private lateinit var espaceRepository: EspaceRepository

    private val espaces = mutableListOf<Espace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spaces)

        // Initialize API
        ApiClient.init(this)

        // Initialize repository
        espaceRepository = EspaceRepository()

        // Initialize RecyclerView
        rvEspaces = findViewById(R.id.rvEspaces)

        spacesAdapter = SpacesAdapter(espaces) { espace ->
            openEspaceDetails(espace)
        }

        rvEspaces.layoutManager = LinearLayoutManager(this)
        rvEspaces.adapter = spacesAdapter

        // Load spaces from backend
        loadEspaces()
    }

    private fun loadEspaces() {

        espaceRepository.getEspaces(object : Callback<List<Espace>> {

            override fun onResponse(
                call: Call<List<Espace>>,
                response: Response<List<Espace>>
            ) {

                if (response.isSuccessful) {

                    val result = response.body()

                    if (result != null) {
                        espaces.clear()
                        espaces.addAll(result.filter { it.actif })
                        spacesAdapter.notifyDataSetChanged()

                        if (espaces.isEmpty()) {
                            Toast.makeText(
                                this@SpacesActivity,
                                "Aucun espace disponible",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    Toast.makeText(
                        this@SpacesActivity,
                        "Erreur serveur : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<List<Espace>>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@SpacesActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun openEspaceDetails(espace: Espace) {

        val intent = Intent(this, EspaceDetailsActivity::class.java)

        intent.putExtra("espace_id", espace.id)

        startActivity(intent)
    }
}
