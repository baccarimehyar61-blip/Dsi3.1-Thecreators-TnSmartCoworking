package tn.smartcoworking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.smartcoworking.R
import tn.smartcoworking.data.api.ApiClient
import tn.smartcoworking.data.model.LoginRequest
import tn.smartcoworking.data.model.LoginResponse
import tn.smartcoworking.data.repository.AuthRepository
import tn.smartcoworking.ui.spaces.SpacesActivity
import tn.smartcoworking.ui.manager.ManagerSpacesActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignUp: TextView

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize API
        ApiClient.init(this)

        // Initialize repository
        authRepository = AuthRepository(this)

        // Connect XML views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)

        // Login button
        btnLogin.setOnClickListener {
            login()
        }

        // Navigate to registration
        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // Validate fields
        if (email.isEmpty()) {
            etEmail.error = "Veuillez saisir votre e-mail"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Veuillez saisir votre mot de passe"
            etPassword.requestFocus()
            return
        }

        btnLogin.isEnabled = false

        val request = LoginRequest(
            email = email,
            motDePasse = password
        )

        authRepository.login(request, object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                btnLogin.isEnabled = true

                if (response.isSuccessful) {

                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Connexion réussie",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate according to role
                        when (loginResponse.role.name) {

                            "MEMBER" -> {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        SpacesActivity::class.java
                                    )
                                )
                            }

                            "MANAGER" -> {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        ManagerSpacesActivity::class.java
                                    )
                                )
                            }

                            else -> {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Rôle non pris en charge",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                        }

                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Réponse vide du serveur",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Échec de connexion : ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<LoginResponse>,
                t: Throwable
            ) {
                btnLogin.isEnabled = true

                Toast.makeText(
                    this@LoginActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
