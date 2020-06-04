package se.mobileinteraction.sleip.ui.details

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.Recording
import se.mobileinteraction.sleip.util.AspectRatioImageView
import java.util.*


class DetailsHorseAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recordingItem = mutableListOf<Recording>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when (viewType) {
            TYPE_COMPLETE -> R.layout.record_item
            else -> R.layout.record_item
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return when (viewType) {
            TYPE_COMPLETE -> RecordingViewHolder(view)
            else -> HeaderViewHolder(view)
        }

    }

    fun setDataRec(listOfRecords: List<Recording>) {
        recordingItem.clear()
        recordingItem.addAll(listOfRecords)
        notifyDataSetChanged()

    }

//    override fun getItemViewType(position: Int): Int {
//        return when(recordingItem[position])
//            is
//    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder ->
                holder.bind(
                )
            is RecordingViewHolder ->
                holder.bind(recordingItem[position])
        }
    }

    /** this part will be used with pending records **/
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val horseBirth = itemView.findViewById<TextView>(R.id.h_birth)
        val horseDesc = itemView.findViewById<TextView>(R.id.h_description)
        val horseImage = itemView.findViewById<AspectRatioImageView>(R.id.photo_item)

        fun bind() {
//            horseBirth.text = horse.date_of_birth
//            horseDesc.text = horse.description
//            horse.image?.let { horseImage.load(it) }
        }

    }

    class RecordingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val recordName = itemView.findViewById<TextView>(R.id.record_name)
        private val recordUploadDate = itemView.findViewById<TextView>(R.id.uploaded_date)
        private val recordForwardBTN = itemView.findViewById<ImageView>(R.id.forward_btn)

        fun bind(recording: Recording) {
            recordName.text =
                if (recording.name.isNullOrEmpty()) ("${recordName.text} #${adapterPosition + 1} ") else recording.name
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val mDate = formatter.parse((recording.uploaded_date)?.dropLast(8)+"Z")
                recordUploadDate.text =   mDate.toString().dropLast(14)

            recordForwardBTN.setOnClickListener {
                //todo send the record id
            }
        }

    }


    override fun getItemCount() =
        recordingItem.size


    companion object {
        const val TYPE_COMPLETE = 0
        const val TYPE_PENDING = 1

    }
}
