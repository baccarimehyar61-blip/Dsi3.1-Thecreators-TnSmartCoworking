package tn.smartcoworking.ui.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.smartcoworking.R
import tn.smartcoworking.data.model.Espace

class ManagerSpacesAdapter(
    private val espaces: MutableList<Espace>,
    private val onModifierClick: (Espace) -> Unit,
    private val onSupprimerClick: (Espace) -> Unit
) : RecyclerView.Adapter<ManagerSpacesAdapter.EspaceViewHolder>() {

    class EspaceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        val tvVille: TextView = itemView.findViewById(R.id.tvVille)
        val tvCapacite: TextView = itemView.findViewById(R.id.tvCapacite)
        val tvPrix: TextView = itemView.findViewById(R.id.tvPrix)

        val btnModifier: Button = itemView.findViewById(R.id.btnModifier)
        val btnSupprimer: Button = itemView.findViewById(R.id.btnSupprimer)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EspaceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_espace, parent, false)

        return EspaceViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EspaceViewHolder,
        position: Int
    ) {
        val espace = espaces[position]

        holder.tvType.text = espace.type.name
        holder.tvNom.text = espace.nom
        holder.tvVille.text = "${espace.ville}, ${espace.adresse}"

        holder.tvCapacite.text =
            "Capacité : ${espace.capacite ?: "Non précisée"} personnes"

        holder.tvPrix.text = "${espace.prixHeure} DT / heure"

        holder.btnModifier.setOnClickListener {
            onModifierClick(espace)
        }

        holder.btnSupprimer.setOnClickListener {
            onSupprimerClick(espace)
        }
    }

    override fun getItemCount(): Int {
        return espaces.size
    }

    fun updateData(newEspaces: List<Espace>) {
        espaces.clear()
        espaces.addAll(newEspaces)
        notifyDataSetChanged()
    }
}
