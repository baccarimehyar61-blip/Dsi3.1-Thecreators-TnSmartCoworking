package isetb.tp3.tunisia_smart_coworking

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registre)

        val edtNom = findViewById<EditText>(R.id.edtNom)
        val edtPrenom = findViewById<EditText>(R.id.edtPrenom)
        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtTelephone = findViewById<EditText>(R.id.edtTelephone)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val layoutProfession = findViewById<LinearLayout>(R.id.layoutProfession)
        val layoutEntreprise = findViewById<LinearLayout>(R.id.layoutEntreprise)
        val edtProfession = findViewById<EditText>(R.id.edtProfession)
        val edtEntreprise = findViewById<EditText>(R.id.edtEntreprise)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val txtLogin = findViewById<TextView>(R.id.txtLogin)

        val types = listOf("Membre", "Gestionnaire")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = spinnerAdapter

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    layoutProfession.visibility = View.VISIBLE
                    layoutEntreprise.visibility = View.GONE
                } else {
                    layoutProfession.visibility = View.GONE
                    layoutEntreprise.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnCreateAccount.setOnClickListener {
            val nom = edtNom.text.toString().trim()
            val prenom = edtPrenom.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val telephone = edtTelephone.text.toString().trim()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()
            val role = if (spinnerType.selectedItemPosition == 0) "MEMBRE" else "GESTIONNAIRE"
            val profession = edtProfession.text.toString().trim()
            val entreprise = edtEntreprise.text.toString().trim()

            if (nom.isEmpty()) {
                edtNom.error = "Le nom est obligatoire."
                return@setOnClickListener
            }
            if (prenom.isEmpty()) {
                edtPrenom.error = "Le prénom est obligatoire."
                return@setOnClickListener
            }
            if (username.isEmpty()) {
                edtUsername.error = "Le username est obligatoire."
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Adresse e-mail invalide."
                return@setOnClickListener
            }
            if (telephone.length != 8) {
                edtTelephone.error = "Le téléphone doit contenir 8 chiffres."
                return@setOnClickListener
            }
            if (password.length < 8) {
                edtPassword.error = "Le mot de passe doit contenir au moins 8 caractères."
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                edtConfirmPassword.error = "Les mots de passe ne correspondent pas."
                return@setOnClickListener
            }
            if (role == "MEMBRE" && profession.isEmpty()) {
                edtProfession.error = "La profession est obligatoire."
                return@setOnClickListener
            }
            if (role == "GESTIONNAIRE" && entreprise.isEmpty()) {
                edtEntreprise.error = "L'entreprise est obligatoire."
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("nom", nom)
                .putString("prenom", prenom)
                .putString("username", username)
                .putString("email", email)
                .putString("telephone", telephone)
                .putString("password", password)
                .putString("role", role)
                .putString("profession", profession)
                .putString("entreprise", entreprise)
                .apply()

            Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
            finish()
        }

        txtLogin.setOnClickListener {
            finish()
        }
    }
}