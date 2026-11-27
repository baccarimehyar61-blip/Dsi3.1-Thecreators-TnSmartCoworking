package tn.smartcoworking.ui.manager

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.R
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.Espace
import tn.smartcoworking.data.model.EspaceRequest
import tn.smartcoworking.data.model.TypeEspace
import tn.smartcoworking.data.repository.EspaceRepository

class EspaceFormActivity : AppCompatActivity() {

    private lateinit var tvFormTitle: TextView
    private lateinit var etNom: EditText
    private lateinit var etDescription: EditText
    private lateinit var etVille: EditText
    private lateinit var etAdresse: EditText
    private lateinit var etCapacite: EditText
    private lateinit var etPrix: EditText
    private lateinit var spType: Spinner
    private lateinit var btnSaveEspace: Button
    private lateinit var btnCancel: Button

    private lateinit var repository: EspaceRepository

    private var espaceId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espace_form)

        ApiClient.init(this)
        repository = EspaceRepository()

        initializeViews()
        setupTypeSpinner()
        setupButtons()

        if (intent.hasExtra("espace_id")) {
            espaceId = intent.getLongExtra("espace_id", -1L)

            if (espaceId == -1L) {
                finish()
                return
            }

            setupEditMode()
            loadEspace(espaceId!!)
        } else {
            setupAddMode()
        }
    }

    private fun initializeViews() {
        tvFormTitle = findViewById(R.id.tvFormTitle)
        etNom = findViewById(R.id.etNom)
        etDescription = findViewById(R.id.etDescription)
        etVille = findViewById(R.id.etVille)
        etAdresse = findViewById(R.id.etAdresse)
        etCapacite = findViewById(R.id.etCapacite)
        etPrix = findViewById(R.id.etPrix)
        spType = findViewById(R.id.spType)
        btnSaveEspace = findViewById(R.id.btnSaveEspace)
        btnCancel = findViewById(R.id.btnCancel)
    }

    // Configure the type spinner
    private fun setupTypeSpinner() {
        val types = TypeEspace.values().map { it.name }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            types
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spType.adapter = adapter
    }

    // Creation mode
    private fun setupAddMode() {
        tvFormTitle.text = "Ajouter un espace"
        btnSaveEspace.text = "Ajouter l'espace"
    }

    // Editing mode
    private fun setupEditMode() {
        tvFormTitle.text = "Modifier un espace"
        btnSaveEspace.text = "Enregistrer les modifications"
    }

    // Load existing space
    private fun loadEspace(id: Long) {

        repository.getEspaceById(id, object : Callback<Espace> {

            override fun onResponse(
                call: Call<Espace>,
                response: Response<Espace>
            ) {
                if (response.isSuccessful) {
                    val espace = response.body()

                    if (espace != null) {
                        fillForm(espace)
                    } else {
                        Toast.makeText(
                            this@EspaceFormActivity,
                            "Espace introuvable",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@EspaceFormActivity,
                        "Erreur serveur : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Espace>, t: Throwable) {
                Toast.makeText(
                    this@EspaceFormActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        })
    }

    // Fill form with existing space data
    private fun fillForm(espace: Espace) {
        etNom.setText(espace.nom)
        etDescription.setText(espace.description)
        etVille.setText(espace.ville)
        etAdresse.setText(espace.adresse)
        etCapacite.setText(espace.capacite?.toString() ?: "")
        etPrix.setText(espace.prixHeure.toString())

        val position = TypeEspace.values().indexOf(espace.type)

        if (position >= 0) {
            spType.setSelection(position)
        }
    }

    private fun setupButtons() {

        btnSaveEspace.setOnClickListener {
            saveEspace()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Validate fields and save
    private fun saveEspace() {

        val nom = etNom.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val ville = etVille.text.toString().trim()
        val adresse = etAdresse.text.toString().trim()
        val capaciteText = etCapacite.text.toString().trim()
        val prixText = etPrix.text.toString().trim().replace(",", ".")

        if (
            nom.isEmpty() ||
            description.isEmpty() ||
            ville.isEmpty() ||
            adresse.isEmpty() ||
            capaciteText.isEmpty() ||
            prixText.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val capacite = capaciteText.toIntOrNull()
        val prix = prixText.toDoubleOrNull()

        if (capacite == null || capacite <= 0) {
            etCapacite.error = "Capacité invalide"
            return
        }

        if (prix == null || prix < 0) {
            etPrix.error = "Prix invalide"
            return
        }

        val type = TypeEspace.values()[spType.selectedItemPosition]

        val request = EspaceRequest(
            nom = nom,
            description = description,
            ville = ville,
            adresse = adresse,
            capacite = capacite,
            prixHeure = prix,
            type = type
        )

        if (espaceId == null) {
            createEspace(request)
        } else {
            updateEspace(espaceId!!, request)
        }
    }

    // POST /api/espaces
    private fun createEspace(request: EspaceRequest) {

        repository.createEspace(request, object : Callback<Espace> {

            override fun onResponse(
                call: Call<Espace>,
                response: Response<Espace>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EspaceFormActivity,
                        "Espace ajouté avec succès",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    Toast.makeText(
                        this@EspaceFormActivity,
                        "Erreur lors de l'ajout : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Espace>, t: Throwable) {
                Toast.makeText(
                    this@EspaceFormActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    // PUT /api/espaces/{id}
    private fun updateEspace(id: Long, request: EspaceRequest) {

        repository.updateEspace(id, request, object : Callback<Espace> {

            override fun onResponse(
                call: Call<Espace>,
                response: Response<Espace>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EspaceFormActivity,
                        "Espace modifié avec succès",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    Toast.makeText(
                        this@EspaceFormActivity,
                        "Erreur lors de la modification : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Espace>, t: Throwable) {
                Toast.makeText(
                    this@EspaceFormActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
