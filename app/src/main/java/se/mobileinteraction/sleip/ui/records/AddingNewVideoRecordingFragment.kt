package se.mobileinteraction.sleip.ui.records

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.video_recording_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import timber.log.Timber
import java.io.File

class AddingNewVideoRecordingFragment : Fragment(R.layout.video_recording_fragment) {

    private val viewModel by viewModel<AddingNewVideoRecordingViewModel>()

    private val recordUri: String
        get() = AddingNewVideoRecordingFragmentArgs.fromBundle(requireArguments()).recordUri

    private val horseId
        get() = AddingNewVideoRecordingFragmentArgs.fromBundle(requireArguments()).horseId


    private var dataSourceFactory: DataSource.Factory? = null
    private lateinit var player: SimpleExoPlayer

    lateinit var mediaSource: ProgressiveMediaSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        upload.setOnClickListener {

            val title = et_title.text.toString()
            val comment = et_comment.text.toString()
            val videoFile: File = File(recordUri)

            viewModel.uploadRecord(horseId, comment, title, videoFile)
        }

        back_to_details.setOnClickListener{
            findNavController().popBackStack()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeDate()
        val orient = resources.configuration.orientation

        if (orient == Configuration.ORIENTATION_LANDSCAPE) {
            hideAll()
        } else {
            showAll()
        }
        initializePlayer()
    }

    private fun initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(context)

        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )
        mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(recordUri.toUri())


        player.prepare(mediaSource, true, true)
        player.playWhenReady = false

        exoplayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        exoplayer.setShutterBackgroundColor(Color.TRANSPARENT)
        exoplayer.player = player
        exoplayer.requestFocus()
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

    override fun onDestroy() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {

            Configuration.ORIENTATION_LANDSCAPE -> {
                hideAll()
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                showAll()

            }
        }
        super.onConfigurationChanged(newConfig)
    }

    private fun observeDate() {
        viewModel.createRecordResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().popBackStack()
            } else if (it?.error != null) {
                Timber.e(it.error)
            }
        })
    }

    private fun hideAll() {
        toolbar.isVisible = false
        title.isVisible = false
        et_title.isVisible = false
        et_comment.isVisible = false
        comment.isVisible = false
        upload.isVisible = false
        upload_text.isVisible = false
        root.setBackgroundColor(resources.getColor(R.color.black, resources.newTheme()))
    }

    private fun showAll() {
        toolbar.isVisible = true
        title.isVisible = true
        et_title.isVisible = true
        et_comment.isVisible = true
        comment.isVisible = true
        upload.isVisible = true
        upload_text.isVisible = true
        root.setBackgroundColor(
            resources.getColor(
                R.color.transparent,
                resources.newTheme()
            )
        )
    }

}
