package id.rashio.android.ui.main.detection

import android.content.SharedPreferences
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

data class DetectionUiState(
    val navigateToHome: Boolean? = null,
    val error: String? = null
)

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val detectionRepository: DetectionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetectionUiState>(DetectionUiState())
    val uiState: StateFlow<DetectionUiState> = _uiState

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
            }
        })

    }


    fun navigateToHome() {
        _uiState.update {
            it.copy(navigateToHome = true)
        }
    }

    fun navigatedToHome() {
        _uiState.update {
            it.copy(navigateToHome = null)
        }
    }

    fun showError(msg: String) {
        _uiState.update {
            it.copy(error = "Upload Failed, $msg")
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }
}