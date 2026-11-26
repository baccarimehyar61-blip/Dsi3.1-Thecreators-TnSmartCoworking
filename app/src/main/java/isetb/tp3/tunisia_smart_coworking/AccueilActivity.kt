package isetb.tp3.tunisia_smart_coworking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AccueilActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtAucun: TextView

    private val espaces = listOf(
        Espace("Startup Corner", "Tunis", R.drawable.startupcorner),
        Espace("Hub Sousse", "Sousse", R.drawable.hubsousse)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val btnProfil = findViewById<Button>(R.id.btnProfil)
        val edtRecherche = findViewById<EditText>(R.id.edtRecherche)
        recyclerView = findViewById(R.id.recyclerViewEspaces)
        txtAucun = findViewById(R.id.txtAucun)

        recyclerView.layoutManager = LinearLayoutManager(this)
        afficherEspaces(espaces)

        edtRecherche.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texte = s.toString().trim()
                val resultat = espaces.filter {
                    it.nom.contains(texte, ignoreCase = true) ||
                            it.ville.contains(texte, ignoreCase = true)
                }
                afficherEspaces(resultat)
            }
        })

        btnProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    private fun afficherEspaces(liste: List<Espace>) {
        txtAucun.visibility = if (liste.isEmpty()) View.VISIBLE else View.GONE

        recyclerView.adapter = EspaceAdapter(liste) { espace ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("nom", espace.nom)
            intent.putExtra("ville", espace.ville)
            intent.putExtra("image", espace.imageResId)
            startActivity(intent)
        }
    }
}