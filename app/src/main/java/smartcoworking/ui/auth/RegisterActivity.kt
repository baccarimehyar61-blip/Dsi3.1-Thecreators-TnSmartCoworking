package tn.smartcoworking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.R
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.RegisterRequest
import tn.smartcoworking.data.model.RegisterResponse
import tn.smartcoworking.data.model.Role
import tn.smartcoworking.data.repository.AuthRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    private lateinit var etNom: EditText
    private lateinit var etPrenom: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelephone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etProfession: EditText
    private lateinit var etSociete: EditText

    private lateinit var spRole: Spinner
    private lateinit var tvProfession: TextView
    private lateinit var tvSociete: TextView
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ApiClient.init(this)
        authRepository = AuthRepository(this)

        // Connect XML views
        etNom = findViewById(R.id.etNom)
        etPrenom = findViewById(R.id.etPrenom)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etTelephone = findViewById(R.id.etTelephone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etProfession = findViewById(R.id.etProfession)
        etSociete = findViewById(R.id.etSociete)

        spRole = findViewById(R.id.spRole)
        tvProfession = findViewById(R.id.tvProfession)
        tvSociete = findViewById(R.id.tvSociete)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        setupRoleSpinner()

        spRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateRoleFields()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnRegister.setOnClickListener {
            register()
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun setupRoleSpinner() {
        val roles = listOf("MEMBER", "MANAGER")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spRole.adapter = adapter
    }

    private fun updateRoleFields() {
        val selectedRole = spRole.selectedItem.toString()

        if (selectedRole == "MEMBER") {
            tvProfession.visibility = View.VISIBLE
            etProfession.visibility = View.VISIBLE

            tvSociete.visibility = View.GONE
            etSociete.visibility = View.GONE
        } else {
            tvProfession.visibility = View.GONE
            etProfession.visibility = View.GONE

            tvSociete.visibility = View.VISIBLE
            etSociete.visibility = View.VISIBLE
        }
    }

    private fun register() {

        val nom = etNom.text.toString().trim()
        val prenom = etPrenom.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telephone = etTelephone.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        val selectedRole = spRole.selectedItem.toString()

        val profession = etProfession.text.toString().trim()
        val societe = etSociete.text.toString().trim()

        // Validate required fields
        if (nom.isEmpty() || prenom.isEmpty() ||
            username.isEmpty() || email.isEmpty() ||
            telephone.isEmpty() || password.isEmpty() ||
            confirmPassword.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Veuillez remplir tous les champs obligatoires",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Les mots de passe ne correspondent pas"
            etConfirmPassword.requestFocus()
            return
        }

        if (selectedRole == "MEMBER" && profession.isEmpty()) {
            etProfession.error = "Veuillez saisir votre profession"
            etProfession.requestFocus()
            return
        }

        if (selectedRole == "MANAGER" && societe.isEmpty()) {
            etSociete.error = "Veuillez saisir votre société"
            etSociete.requestFocus()
            return
        }

        val role = if (selectedRole == "MEMBER") {
            Role.MEMBER
        } else {
            Role.MANAGER
        }

        val request = RegisterRequest(
            nom = nom,
            prenom = prenom,
            username = username,
            email = email,
            motDePasse = password,
            telephone = telephone,
            role = role,
            profession = if (role == Role.MEMBER) profession else null,
            societe = if (role == Role.MANAGER) societe else null
        )

        btnRegister.isEnabled = false

        authRepository.register(
            request,
            object : Callback<RegisterResponse> {

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    btnRegister.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {

                        Toast.makeText(
                            this@RegisterActivity,
                            "Compte créé avec succès !",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(
                            this@RegisterActivity,
                            LoginActivity::class.java
                        )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP

                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Échec de l'inscription : ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<RegisterResponse>,
                    t: Throwable
                ) {
                    btnRegister.isEnabled = true

                    Toast.makeText(
                        this@RegisterActivity,
                        "Erreur réseau : ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
