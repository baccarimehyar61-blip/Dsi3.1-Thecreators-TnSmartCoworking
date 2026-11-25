package isetb.tp3.tunisia_smart_coworking

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtForgot = findViewById<TextView>(R.id.txtForgot)
        val txtRegister = findViewById<TextView>(R.id.txtRegister)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            if (email.isEmpty()) {
                edtEmail.error = "L'e-mail est obligatoire."
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Adresse e-mail invalide."
                return@setOnClickListener
            }
            if (password.length < 8) {
                edtPassword.error = "Le mot de passe doit contenir au moins 8 caractères."
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val savedEmail = prefs.getString("email", null)
            val savedPassword = prefs.getString("password", null)

            if (email == savedEmail && password == savedPassword) {
                startActivity(Intent(this, AccueilActivity::class.java))
            } else {
                Toast.makeText(this, "E-mail ou mot de passe incorrect.", Toast.LENGTH_SHORT).show()
            }
        }

        txtForgot.setOnClickListener {
            Toast.makeText(this, "Fonctionnalité bientôt disponible.", Toast.LENGTH_SHORT).show()
        }

        txtRegister.setOnClickListener {
            startActivity(Intent(this, RegistreActivity::class.java))
        }
    }
}