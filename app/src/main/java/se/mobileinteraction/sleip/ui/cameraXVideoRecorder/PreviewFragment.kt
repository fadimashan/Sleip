package se.mobileinteraction.sleip.ui.cameraXVideoRecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.preview_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.MainActivity
import se.mobileinteraction.sleip.R
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PreviewFragment : Fragment(R.layout.preview_fragment) {

    private val viewModel by viewModel<PreviewViewModel>()
    private lateinit var outputDirectory: File
    private var filename: String? = ""

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy {
        ProcessCameraProvider.getInstance(
            requireContext()
        )
    }

    private val horseId
        get() = PreviewFragmentArgs.fromBundle(requireArguments()).horseId

    private lateinit var cameraSelector: CameraSelector
    private var videoCapture: VideoCapture? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rec_video.isVisible = true
        stop_video.isVisible = false
        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        if (allPermissionsGranted()) {
            camera_preview.post {
                setupCamera()
            }
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        rec_video.setOnClickListener {
            if (allPermissionsGranted()) {
                rec_video.isVisible = false
                stop_video.isVisible = true
                startRec()
            } else {
                requestPermissions(
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }

        }
        stop_video.setOnClickListener {
            rec_video.isVisible = true
            stop_video.isVisible = false
            videoCapture?.stopRecording()
        }

    }


    override fun onDestroy() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        super.onDestroy()
    }

    private fun setupCamera() {
        try {
            cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            val cameraProvider = cameraProviderFuture.get()
            val pcp =
                cameraProvider.apply {
                    try {
                        buildVideoRecorderConfig()
                        bindToLifecycle(
                            this@PreviewFragment,
                            cameraSelector,
                            buildPreviewUseCase(),
                            videoCapture
                        )
                    } catch (e: Exception) {
                        requireActivity().requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        findNavController().popBackStack()
                    }
                }
            cameraProviderFuture.addListener(Runnable {
                pcp
            }, ContextCompat.getMainExecutor(requireContext()))

        } catch (e: Exception) {
            requireActivity().requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            findNavController().popBackStack()
            Log.e("TAG", "Error in the camera setup ")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildVideoRecorderConfig() {
        videoCapture = UsecaseConfigBuilder.buildVideoConfig(
            camera_preview.display,
            camera_preview.width / camera_preview.height
        )
    }

    private fun buildPreviewUseCase() = UsecaseConfigBuilder.buildPreviewConfig(
        camera_preview.display,
        camera_preview.width / camera_preview.height
    ).also {
        it.setSurfaceProvider(camera_preview.previewSurfaceProvider)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                camera_preview.post { setupCamera() }
                startRec()
            } else {
                requestPermissions(
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }


    @SuppressLint("RestrictedApi")
    fun startRec() {
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmss")
        val dateFormatted = date.format(formatter)
        filename = "Rec$dateFormatted.mp4"
        videoCapture.let {
            val newVideoDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path + File.separator + SLEIP_DIRECTORY_PATH
            val file = File(newVideoDir, filename)

            if (videoCapture != null) {
                videoCapture!!.startRecording(
                    file, ContextCompat.getMainExecutor(requireContext()),
                    object : VideoCapture.OnVideoSavedCallback {
                        override fun onVideoSaved(file: File) {
                            val videoPath = file.absolutePath
                            val uri = Uri.fromFile(file)
                            videoPath
                            uri
                            findNavController().navigate(
                                PreviewFragmentDirections.actionPreviewFragmentToVideoRecordingFragment(
                                    videoPath, horseId
                                )
                            )
                            Log.i(TAG, "Video File : $file")
                        }

                        override fun onError(
                            videoCaptureError: Int,
                            message: String,
                            cause: Throwable?
                        ) {
                            Log.i(TAG, "Video Error: $message")
                        }

                    })
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }

    }


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val VIDEO_EXTENSION = ".mp4"
        private const val TAG = "tag"
        const val VIDEO_TYPE = "video/mp4"
        const val SLEIP_DIRECTORY_PATH = "/Sliep/"
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }
}
