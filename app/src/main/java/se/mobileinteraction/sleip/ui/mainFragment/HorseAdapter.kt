package se.mobileinteraction.sleip.ui.mainFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.util.load

class HorseAdapter(val onItemClick: (Horse) -> Unit) : RecyclerView.Adapter<HorseAdapter.HorseViewHolder>() {

    private var list = mutableListOf<Horse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HorseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_horse, parent, false
            )
        )

    fun setData(listOfHorses: List<Horse>) {
        list.clear()
        list.addAll(listOfHorses)
        notifyDataSetChanged()

    }

    fun removeData(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun update(modelList: ArrayList<Horse>) {
        list = modelList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HorseViewHolder, position: Int) {

        val item = list[position]

        holder.bind(item,onItemClick)
    }

    class HorseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val horseName = itemView.findViewById<TextView>(R.id.horse_name)
        val horseBirth = itemView.findViewById<TextView>(R.id.horse_birth)
        val horseDesc = itemView.findViewById<TextView>(R.id.horse_desc)
        val horseImg = itemView.findViewById<CircularImageView>(R.id.img_horse)

        fun bind(horse: Horse, onItemClick:(Horse) -> Unit) {
            horseName.text = horse.name
            horseBirth.text = horse.date_of_birth
            horseDesc.text = horse.description
            horse.image?.let { horseImg.load(it) }


            itemView.setOnClickListener {
                onItemClick(horse)
            }


        }

    }

    override fun getItemCount() = list.size

}