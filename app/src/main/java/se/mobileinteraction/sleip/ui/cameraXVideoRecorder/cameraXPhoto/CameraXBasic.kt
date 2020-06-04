package se.mobileinteraction.sleip.ui.cameraXVideoRecorder.cameraXPhoto

import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig


class CameraXBasic : CameraXConfig.Provider {
     override fun getCameraXConfig(): CameraXConfig {
         return Camera2Config.defaultConfig()
     }
}