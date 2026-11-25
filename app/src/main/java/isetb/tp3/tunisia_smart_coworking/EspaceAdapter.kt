package isetb.tp3.tunisia_smart_coworking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EspaceAdapter(
    private val espaces: List<Espace>,
    private val onItemClick: (Espace) -> Unit
) : RecyclerView.Adapter<EspaceAdapter.EspaceViewHolder>() {

    class EspaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageEspace)
        val nom: TextView = itemView.findViewById(R.id.textEspaceNom)
        val ville: TextView = itemView.findViewById(R.id.textEspaceVille)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_espace, parent, false)
        return EspaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspaceViewHolder, position: Int) {
        val espace = espaces[position]
        holder.image.setImageResource(espace.imageResId)
        holder.nom.text = espace.nom
        holder.ville.text = espace.ville
        holder.itemView.setOnClickListener { onItemClick(espace) }
    }

    override fun getItemCount(): Int = espaces.size
}