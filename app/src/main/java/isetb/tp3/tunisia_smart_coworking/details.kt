package isetb.tp3.tunisia_smart_coworking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val nom = intent.getStringExtra("nom")
        val ville = intent.getStringExtra("ville")
        val image = intent.getIntExtra("image", R.drawable.startupcorner)

        findViewById<ImageView>(R.id.imageDetail).setImageResource(image)
        findViewById<TextView>(R.id.textNomDetail).text = nom
        findViewById<TextView>(R.id.textVilleDetail).text = ville

        findViewById<Button>(R.id.btnReserve).setOnClickListener {
            val i = Intent(this, ReservationActivity::class.java)
            i.putExtra("nom", nom)
            startActivity(i)
        }
    }
}