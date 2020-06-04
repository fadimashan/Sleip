package se.mobileinteraction.sleip.ui.details

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.AnalysisStatus
import se.mobileinteraction.sleip.entities.RecordingResponse
import java.util.*


class DetailsHorseAdapter(val playVideo: (RecordingResponse) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recordsList = mutableListOf<Any>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when (viewType) {
            TYPE_COMPLETE -> R.layout.item_complete_record
            TYPE_PENDING -> R.layout.item_pending_record
            TYPE_HEADER -> R.layout.item_header
            else -> 3
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return when (viewType) {
            TYPE_COMPLETE -> RecordingViewHolder(view)
            TYPE_PENDING -> RecordingViewHolder(view)
            TYPE_HEADER -> HeaderViewHolder(view)
            else -> HeaderViewHolder(view)
        }

    }

    fun setDataRec(listOfRecords: List<Any>) {
        recordsList.addAll(listOfRecords)
        notifyDataSetChanged()

    }


    override fun getItemViewType(position: Int): Int {
        if (recordsList[position] is HeaderItem) return TYPE_HEADER
        return if ((recordsList[position] as RecordingResponse).status == AnalysisStatus.complete) TYPE_COMPLETE
        else TYPE_PENDING

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder ->
                holder.bind(
                    (recordsList as List<HeaderItem>)[position]
                )
            is RecordingViewHolder ->
                holder.bind((recordsList as List<RecordingResponse>)[position], playVideo)
        }
    }

    /** this part will be used with pending records **/
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val headerItem = itemView.findViewById<TextView>(R.id.status_case)

        fun bind(item: HeaderItem) {
            headerItem.text = item.text


        }

    }

    class RecordingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val recordName = itemView.findViewById<TextView>(R.id.record_name)
        private val recordUploadDate = itemView.findViewById<TextView>(R.id.uploaded_date)

        fun bind(recording: RecordingResponse, playVideo: (RecordingResponse) -> Unit) {
            recordName.text =
                if (recording.name.isNullOrEmpty()) ("My Record #${adapterPosition + 1}") else recording.name
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val mDate = formatter.parse((recording.uploaded_date)?.dropLast(8) + "Z")
            recordUploadDate.text = mDate.toString().dropLast(14)
            itemView.setOnClickListener {
                playVideo(recording)
            }
        }

    }


    override fun getItemCount() = recordsList.size


    companion object {
        const val TYPE_COMPLETE = 0
        const val TYPE_PENDING = 1
        const val TYPE_HEADER = 2

    }
}
