package nz.atomic.jongleur

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyzer : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        image.close()
//        TODO("Not yet implemented")
    }
}