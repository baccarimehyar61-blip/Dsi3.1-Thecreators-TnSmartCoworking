package isetb.tp3.tunisia_smart_coworking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReservationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val edtNom = findViewById<EditText>(R.id.edtNom)
        val edtDate = findViewById<EditText>(R.id.edtDate)
        val edtHeures = findViewById<EditText>(R.id.edtHeures)
        val btnConfirmer = findViewById<Button>(R.id.btnConfirmer)

        btnConfirmer.setOnClickListener {
            val espace = edtNom.text.toString()
            val date = edtDate.text.toString()
            val heures = edtHeures.text.toString()

            if (espace.isEmpty() || date.isEmpty() || heures.isEmpty()) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Réservation confirmée : $espace le $date ($heures h)",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
}