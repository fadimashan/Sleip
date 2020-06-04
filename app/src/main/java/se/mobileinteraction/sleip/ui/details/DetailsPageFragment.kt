package se.mobileinteraction.sleip.ui.details

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity
import kotlinx.android.synthetic.main.details_page_fragment.*
import kotlinx.android.synthetic.main.item_header.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.AnalysisStatus
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.entities.RecordingResponse
import se.mobileinteraction.sleip.util.afterMeasure
import se.mobileinteraction.sleip.util.load
import timber.log.Timber


class DetailsPageFragment : Fragment(R.layout.details_page_fragment) {


    private val viewModel: DetailsPageViewModel by viewModel {
        parametersOf(horse)
    }

    private val horse: Horse
        get() = DetailsPageFragmentArgs.fromBundle(requireArguments()).horseDetails


    var recordsList = mutableListOf<Any>()
    var pendingList = mutableListOf<Any>()
    var completedList = mutableListOf<Any>()

    private val detailsAdapter = DetailsHorseAdapter { Recording -> playVideo(Recording) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recyclerView.afterMeasure {}
        recyclerView.apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
             addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

            val divider =
                ContextCompat.getDrawable(requireContext(), R.drawable.divider)
            val itemDecoration = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
            itemDecoration.setDrawable(divider!!).apply {
                    setPadding(2,0,2,0)
                }
        }

        recyclerView.post {

            viewModel.getHorse(horse.id)
            horse.image?.let {
                detail_photo.load(it)
            }

            viewModel.getRecList(horse.id)

        }

        back_btn.setOnClickListener {
            findNavController().popBackStack()
        }

        back.setOnClickListener {
            findNavController().popBackStack()
        }

        rec_video.setOnClickListener {
            showPictureDialog()
        }

    }

    private fun playVideo(recording: RecordingResponse) {
        findNavController().navigate(
            DetailsPageFragmentDirections.actionDetailsPageFragmentToRecordsDetailsFragment(
                recording
            )
        )

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select video from gallery", "Record video from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> chooseVideoFromGallery()
                1 -> takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun chooseVideoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    @SuppressLint("CheckResult")
    private fun takeVideoFromCamera() {
        findNavController().navigate(
            DetailsPageFragmentDirections.actionDetailsPageFragmentToPreviewFragment(
                horse.id
            )
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Timber.d("%s", resultCode)

        if (resultCode == Activity.RESULT_CANCELED) {
            Timber.d("cancel")
            return
        }
        if (requestCode == GALLERY) {
            Timber.d("gale")
            if (data != null) {
                val contentURI = data.data

                val selectedVideoPath = getPath(contentURI)
                Timber.d(selectedVideoPath)

                findNavController().navigate(
                    DetailsPageFragmentDirections.actionDetailsPageFragmentToVideoRecordingFragment(
                        selectedVideoPath!!, horse.id
                    )
                )

            }

        } else if (resultCode == Activity.RESULT_OK) {
            Timber.d("camera")
            val fileUri = data!!.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME)
            Timber.d(fileUri!!.toUri().toString())

            findNavController().navigate(
                DetailsPageFragmentDirections.actionDetailsPageFragmentToVideoRecordingFragment(
                    fileUri.toUri().toString(), horse.id
                )
            )
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } else
            null
    }

    override fun onResume() {
        detailsAdapter.recordsList.clear()
        super.onResume()
    }

    private fun observeData() {
        viewModel.horseProfile.observe(viewLifecycleOwner, Observer {
            it.image?.let { it1 -> detail_photo.load(it1) }
            h_birth.text = it.date_of_birth
            h_description.text = it.description
            horse_name.text = it.name
        })

        viewModel.horseRec.observe(viewLifecycleOwner, Observer { it ->

            recordsList.clear()
            recordsList.addAll(it)
            pendingList = recordsList.filter {
                (it as RecordingResponse).status == AnalysisStatus.pending
            } as MutableList<Any>
            if (pendingList.isNotEmpty()) pendingList.add(0, HeaderItem("Awaiting Results"))
            completedList = recordsList.filter {
                (it as RecordingResponse).status == AnalysisStatus.complete
            } as MutableList<Any>
            if (completedList.isNotEmpty()) completedList.add(0,HeaderItem("Results"))

            detailsAdapter.setDataRec(pendingList)
            detailsAdapter.setDataRec(completedList)

        })
    }

    companion object {
        const val GALLERY = 1
        const val RequestCode = 101
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
