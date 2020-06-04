package se.mobileinteraction.sleip.ui.records.analysis

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.analysis_result_fragment.*
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.RecordingResponse

class AnalysisResultFragment : Fragment(R.layout.analysis_result_fragment) {

    private var dataSourceFactory: DataSource.Factory? = null
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: ProgressiveMediaSource
    private val video: RecordingResponse
        get() =
            AnalysisResultFragmentArgs.fromBundle(requireArguments()).videoAnalysis

    private val analysisAdapter = AnalysisAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllTheViewsWithRecyclerview()

        back_to_horse_details.setOnClickListener {
            findNavController().popBackStack()
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
                .createMediaSource(Uri.parse(video.analysis?.video))


        player.prepare(mediaSource, true, true)
        player.playWhenReady = false

        analysis_video_player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        analysis_video_player.setShutterBackgroundColor(Color.TRANSPARENT)
        analysis_video_player.player = player
        analysis_video_player.requestFocus()
    }

    private fun initAllTheViewsWithRecyclerview() {
        analysis_list.apply {
            adapter = analysisAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        analysisAdapter.setDataRec(video.analysis!!.analysisimage_set)
        comment_result.text = video.analysis!!.comment
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

}