package isetb.tp3.tunisia_smart_coworking

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val txtRole = findViewById<TextView>(R.id.txtRole)
        val edtNom = findViewById<EditText>(R.id.edtNom)
        val edtPrenom = findViewById<EditText>(R.id.edtPrenom)
        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtTelephone = findViewById<EditText>(R.id.edtTelephone)
        val layoutProfession = findViewById<LinearLayout>(R.id.layoutProfession)
        val layoutEntreprise = findViewById<LinearLayout>(R.id.layoutEntreprise)
        val edtProfession = findViewById<EditText>(R.id.edtProfession)
        val edtEntreprise = findViewById<EditText>(R.id.edtEntreprise)
        val btnEnregistrer = findViewById<Button>(R.id.btnEnregistrer)
        val btnDeconnexion = findViewById<Button>(R.id.btnDeconnexion)

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = prefs.getString("role", "MEMBRE")

        edtNom.setText(prefs.getString("nom", ""))
        edtPrenom.setText(prefs.getString("prenom", ""))
        edtUsername.setText(prefs.getString("username", ""))
        edtEmail.setText(prefs.getString("email", ""))
        edtTelephone.setText(prefs.getString("telephone", ""))
        edtProfession.setText(prefs.getString("profession", ""))
        edtEntreprise.setText(prefs.getString("entreprise", ""))

        if (role == "MEMBRE") {
            txtRole.text = "Membre"
            layoutProfession.visibility = View.VISIBLE
            layoutEntreprise.visibility = View.GONE
        } else {
            txtRole.text = "Gestionnaire"
            layoutProfession.visibility = View.GONE
            layoutEntreprise.visibility = View.VISIBLE
        }

        btnEnregistrer.setOnClickListener {
            val nom = edtNom.text.toString().trim()
            val prenom = edtPrenom.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val telephone = edtTelephone.text.toString().trim()

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

            prefs.edit()
                .putString("nom", nom)
                .putString("prenom", prenom)
                .putString("username", username)
                .putString("email", email)
                .putString("telephone", telephone)
                .putString("profession", edtProfession.text.toString().trim())
                .putString("entreprise", edtEntreprise.text.toString().trim())
                .apply()

            Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show()
        }

        btnDeconnexion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}