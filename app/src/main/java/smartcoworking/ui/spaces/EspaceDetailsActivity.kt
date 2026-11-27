package tn.smartcoworking.ui.spaces

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.R
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.Espace
import tn.smartcoworking.data.repository.EspaceRepository

class EspaceDetailsActivity : AppCompatActivity() {

    private lateinit var repository: EspaceRepository

    private lateinit var tvDetailType: TextView
    private lateinit var tvDetailNom: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var tvDetailVille: TextView
    private lateinit var tvDetailAdresse: TextView
    private lateinit var tvDetailCapacite: TextView
    private lateinit var tvDetailPrix: TextView

    private lateinit var btnReserver: Button
    private lateinit var btnRetour: Button

    private var espaceId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espace_details)

        ApiClient.init(this)
        repository = EspaceRepository()

        initializeViews()

        espaceId = intent.getLongExtra("espace_id", -1L)

        if (espaceId == -1L) {
            Toast.makeText(this, "Espace introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupButtons()
        loadEspaceDetails()
    }

    private fun initializeViews() {

        tvDetailType = findViewById(R.id.tvDetailType)
        tvDetailNom = findViewById(R.id.tvDetailNom)
        tvDetailDescription = findViewById(R.id.tvDetailDescription)
        tvDetailVille = findViewById(R.id.tvDetailVille)
        tvDetailAdresse = findViewById(R.id.tvDetailAdresse)
        tvDetailCapacite = findViewById(R.id.tvDetailCapacite)
        tvDetailPrix = findViewById(R.id.tvDetailPrix)

        btnReserver = findViewById(R.id.btnReserver)
        btnRetour = findViewById(R.id.btnRetour)
    }

    private fun setupButtons() {

        btnRetour.setOnClickListener {
            finish()
        }

        btnReserver.setOnClickListener {
            Toast.makeText(
                this,
                "La réservation sera disponible prochainement",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadEspaceDetails() {

        repository.getEspaceById(
            espaceId,
            object : Callback<Espace> {

                override fun onResponse(
                    call: Call<Espace>,
                    response: Response<Espace>
                ) {

                    if (response.isSuccessful) {

                        val espace = response.body()

                        if (espace != null) {
                            displayEspace(espace)
                        } else {
                            Toast.makeText(
                                this@EspaceDetailsActivity,
                                "Espace introuvable",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                    } else {
                        Toast.makeText(
                            this@EspaceDetailsActivity,
                            "Erreur serveur : ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Espace>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@EspaceDetailsActivity,
                        "Erreur réseau : ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun displayEspace(espace: Espace) {

        tvDetailType.text = espace.type.name
        tvDetailNom.text = espace.nom
        tvDetailDescription.text = espace.description
        tvDetailVille.text = espace.ville
        tvDetailAdresse.text = espace.adresse

        tvDetailCapacite.text =
            "Capacité : ${espace.capacite ?: "Non précisée"} personnes"

        tvDetailPrix.text = "${espace.prixHeure} DT / heure"
    }
}
