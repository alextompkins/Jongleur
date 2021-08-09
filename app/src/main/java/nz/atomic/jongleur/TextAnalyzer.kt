package nz.atomic.jongleur

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions

class TextAnalyzer : ImageAnalysis.Analyzer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var inProgress = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (inProgress) return  // Early return if we are already processing an image

        imageProxy.image?.let {
            inProgress = true

            // Pass image to an ML Kit Vision API
            val inputImage = InputImage.fromMediaImage(it, 0) // TODO handle rotation properly
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    if (visionText.text.isBlank()) {
                        Log.i(TAG, "No text found.")
                    } else {
                        Log.i(TAG, "Text found: \n${visionText.text}")
                    }
                    inProgress = false
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Text analyzer error: ${e.message}", e)
                    inProgress = false
                    imageProxy.close()
                }
        }
    }

    companion object {
        private const val TAG = "TEXT_ANALYZER"
    }
}