package se.mobileinteraction.sleip.ui.records

import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.record_details_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.AnalysisStatus
import se.mobileinteraction.sleip.entities.RecordingResponse

class RecordsDetailsFragment : Fragment(R.layout.record_details_layout) {


    private val viewModel by viewModel<RecordDetailsViewModel>()

    private val video: RecordingResponse
        get() =
            RecordsDetailsFragmentArgs.fromBundle(requireArguments()).recordDetails

    private var dataSourceFactory: DataSource.Factory? = null
    private lateinit var player: SimpleExoPlayer

    private lateinit var mediaSource: ProgressiveMediaSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRecordInformationFromServer()
        back_to_records_list.setOnClickListener {
            findNavController().popBackStack()
        }

        analysis_btn.isEnabled = video.analysis != null

        analysis_btn.setOnClickListener {
            findNavController().navigate(
                RecordsDetailsFragmentDirections.actionRecordsDetailsFragmentToAnalysisResultFragment(
                    video
                )
            )
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializePlayer()
    }

    private fun initializePlayer() {


        player = ExoPlayerFactory.newSimpleInstance(context)

        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )
        mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(video.video))


        player.prepare(mediaSource, true, true)
        player.playWhenReady = false
        exoplayer_player.player = player
        exoplayer_player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        exoplayer_player.setShutterBackgroundColor(Color.TRANSPARENT)
        exoplayer_player.requestFocus()
    }


    override fun onResume() {
        super.onResume()
        player.stop()
        player.release()
        initializePlayer()
    }

    private fun releasePlayer() {
        player.release()
    }

    override fun onPause() {
        super.onPause()
        player.stop()
        player.release()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
        releasePlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {

            Configuration.ORIENTATION_LANDSCAPE -> {

                toolbar.isVisible = false
                name.isVisible = false
                et_name.isVisible = false
                comment.isVisible = false
                et_comment.isVisible = false
                uploaded_date.isVisible = false
                et_uploaded_date.isVisible = false
                status.isVisible = false
                et_status.isVisible = false
                rec_root.setBackgroundColor(resources.getColor(R.color.black, resources.newTheme()))
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                toolbar.isVisible = true
                name.isVisible = true
                et_name.isVisible = true
                comment.isVisible = true
                et_comment.isVisible = true
                uploaded_date.isVisible = true
                et_uploaded_date.isVisible = true
                status.isVisible = true
                et_status.isVisible = true
                rec_root.setBackgroundColor(
                    resources.getColor(
                        R.color.transparent,
                        resources.newTheme()
                    )
                )

            }
        }
        super.onConfigurationChanged(newConfig)
    }

    private fun fetchRecordInformationFromServer() {
        et_name.text = video.name
        et_comment.text = video.comment
        et_uploaded_date.text = video.uploaded_date
        et_status.text = when (video.status) {
            AnalysisStatus.complete -> "We have a results!"
            AnalysisStatus.pending -> "Awaiting Result"
            null -> "Awaiting Result"
        }

    }

}