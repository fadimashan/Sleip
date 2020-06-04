package se.mobileinteraction.sleip.ui.cameraXVideoRecorder

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.core.impl.VideoCaptureConfig


object UsecaseConfigBuilder {

    fun buildPreviewConfig(display: Display, aspectRatio: Int): Preview {
        DisplayMetrics().also { display.getMetrics(it) }
        return Preview.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setTargetRotation(display.rotation)
            .build()
    }

    /** Photo configuration **/
    fun buildImageAnalysisConfig(display: Display, aspectRatio: Int): ImageAnalysis {
        DisplayMetrics().also { display.getMetrics(it) }
        return ImageAnalysis.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setTargetRotation(display.rotation)
            .setImageQueueDepth(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
            .build()
    }

    @SuppressLint("RestrictedApi")
    fun buildVideoConfig(display: Display, aspectRatio: Int): VideoCapture? {
        DisplayMetrics().also { display.getMetrics(it) }

        return try {
            VideoCaptureConfig.Builder()
//                 .setDefaultResolution(Size(3840 or 4096, 2160))
                .setTargetAspectRatio(aspectRatio)
                .setAudioRecordSource(MediaRecorder.AudioSource.MIC)

                .build()


        } catch (e: Exception) {
            Log.e("TAG", "NOT 4K")
            null
        }
    }


}