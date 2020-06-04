package se.mobileinteraction.sleip.ui.records.analysis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.AnalysisImage
import se.mobileinteraction.sleip.util.AspectRatioImageView
import se.mobileinteraction.sleip.util.load


class AnalysisAdapter() :
    RecyclerView.Adapter<AnalysisAdapter.GraphViewHolder>() {

    var itemsList = mutableListOf<AnalysisImage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
       GraphViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
        )


    fun setDataRec(listOfRecords: List<AnalysisImage>) {
        itemsList.addAll(listOfRecords)
        notifyDataSetChanged()

    }


    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        holder.bind(itemsList[position])

    }


    class GraphViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = itemView.findViewById<AspectRatioImageView>(R.id.image)
        fun bind(image: AnalysisImage) {
            imageView.load(image.image)
//            imageView.aspectRatio = (imageView.height.toDouble()/imageView.width)
        }

    }


    override fun getItemCount() = itemsList.size

}
