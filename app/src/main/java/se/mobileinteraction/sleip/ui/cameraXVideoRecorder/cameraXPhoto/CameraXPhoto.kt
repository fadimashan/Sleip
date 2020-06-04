package se.mobileinteraction.sleip.ui.cameraXVideoRecorder.cameraXPhoto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.camerax_photo_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.ui.cameraXVideoRecorder.UsecaseConfigBuilder


class CameraXPhoto :Fragment(R.layout.camerax_photo_fragment) {
    private val viewModel by viewModel<CameraXPhotoViewModel>()
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy { ProcessCameraProvider.getInstance(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            photo_preview.post { setupCamera() }
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupCamera() {
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                buildImageAnalysisUseCase(),
                buildPreviewUseCase()
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun buildImageAnalysisUseCase() = UsecaseConfigBuilder.buildImageAnalysisConfig(
        photo_preview.display,
        photo_preview.width / photo_preview.height
    ).apply {
       // setAnalyzer(ContextCompat.getMainExecutor(requireContext()) , /** set analyzer **/ )
    }

    private fun buildPreviewUseCase() = UsecaseConfigBuilder.buildPreviewConfig(
        photo_preview.display,
        photo_preview.width / photo_preview.height
    ).also {
        it.setSurfaceProvider(photo_preview.previewSurfaceProvider)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                photo_preview.post { setupCamera() }
            } else {
                requestPermissions(
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}