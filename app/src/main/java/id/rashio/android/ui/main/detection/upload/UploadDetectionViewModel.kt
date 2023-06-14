package id.rashio.android.ui.main.detection.upload

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.DetectionRepository
import id.rashio.android.model.FileUploadResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

//data class UploadDetectionUiState(
//    val navigateToResult: Boolean? = null,
//    val error: String? = null
//)

sealed interface UploadDetectionUiState {
    object Loading : UploadDetectionUiState
    data class Success(
        val navigateToResult: Boolean? = null,
        val diseaseName: String? = null,
        val percentage: Float? = null,
        val imageUrl: String? = null
    ) : UploadDetectionUiState

    data class Error(val message: String? = null) : UploadDetectionUiState
}

@HiltViewModel
class UploadDetectionViewModel @Inject constructor(
    private val detectionRepository: DetectionRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UploadDetectionUiState>(UploadDetectionUiState.Success())
    val uiState: StateFlow<UploadDetectionUiState> = _uiState

    fun uploadFile(photo: File) {
        val call = detectionRepository.uploadDetection(photo)
        call.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {

                    Log.d(
                        "File Uploaded",
                        "${response.body()?.code}, ${response.body()?.message}, ${response.body()?.data}"
                    )

                    val diseaseId = response.body()?.data?.result ?: ""
                    val percentage = response.body()?.data?.percentage ?: 0F
                    val imageUrl = response.body()?.data?.imageUrl ?: ""

                    navigateToResult(diseaseId, percentage, imageUrl)

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    showError(message ?: "Unexpected Error")
                    Log.d(
                        "File Upload Failed",
                        "${response.body()?.code}, ${response.body()?.message}, ${response.body()?.data}"
                    )

                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                val message = t.message
                showError(message ?: "Unexpected Error")
            }
        })

    }


    fun navigateToResult(diseaseName: String, percentage: Float, imageUrl: String) {
        _uiState.update {
            UploadDetectionUiState.Success(true, diseaseName, percentage, imageUrl)
        }
    }

    fun navigatedToResult() {
        _uiState.update {
            UploadDetectionUiState.Success(null, null, null)
        }
    }

    fun showError(msg: String) {
        _uiState.update {
            UploadDetectionUiState.Error("Uploaded Failed, $msg")
        }
    }

    fun errorShown() {
        _uiState.update {
            UploadDetectionUiState.Error(null)
        }
    }
}