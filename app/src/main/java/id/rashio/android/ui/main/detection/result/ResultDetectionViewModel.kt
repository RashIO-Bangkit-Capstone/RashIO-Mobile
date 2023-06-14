package id.rashio.android.ui.main.detection.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.DiseaseRepository
import id.rashio.android.model.DiseaseResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

sealed interface ResultDetectionUiState {
    object Loading : ResultDetectionUiState
    data class Success(
        val disease: DiseaseResponse,
        val percentage: Float? = null,
        val imageUrl: String?
    ) :
        ResultDetectionUiState

    data class Error(val message: String) : ResultDetectionUiState
}

@HiltViewModel
class ResultDetectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diseaseRepository: DiseaseRepository
) : ViewModel() {

    private val diseaseName: String? = savedStateHandle["diseaseName"]
    private val percentage: Float? = savedStateHandle["percentage"]
    private val imageUrl: String? = savedStateHandle["imageUrl"]


    private val _uiState = MutableLiveData<ResultDetectionUiState>()
    val uiState: LiveData<ResultDetectionUiState> = _uiState

    init {
        if (diseaseName != null) {
            getDisease(diseaseName)
        }
    }

    private fun getDisease(diseaseName: String) {
        val call = diseaseRepository.getDisease(diseaseName)
        call.enqueue(object : Callback<DiseaseResponse> {
            override fun onResponse(
                call: Call<DiseaseResponse>,
                response: Response<DiseaseResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    _uiState.postValue(
                        data?.let { ResultDetectionUiState.Success(it, percentage, imageUrl) }
                    )

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    _uiState.postValue(
                        message?.let { ResultDetectionUiState.Error(it) }
                    )
                }
            }

            override fun onFailure(call: Call<DiseaseResponse>, t: Throwable) {

            }
        })
    }


}