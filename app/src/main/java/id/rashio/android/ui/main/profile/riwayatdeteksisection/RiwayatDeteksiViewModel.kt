package id.rashio.android.ui.main.profile.riwayatdeteksisection

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.HistoryDetectionRepository
import id.rashio.android.model.PredictionLogs
import id.rashio.android.model.RiwayatDeteksiResponse
import id.rashio.android.utils.TokenManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class RiwayatDeteksiViewModel @Inject constructor(
    private val historyDetectionRepository: HistoryDetectionRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _allHistory = MutableLiveData<List<PredictionLogs>?>()
    val allHistory: MutableLiveData<List<PredictionLogs>?> = _allHistory

    private val tokenManager = TokenManager(sharedPreferences)


    init {
        getHistory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistory() {
        getId()?.let { id ->
            val call = historyDetectionRepository.getAllHistory(id)
            call.enqueue(object : Callback<RiwayatDeteksiResponse> {
                @SuppressLint("LongLogTag")
                override fun onResponse(
                    call: Call<RiwayatDeteksiResponse>,
                    response: Response<RiwayatDeteksiResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        val predictionLogs = data?.predictionLogs
                        _allHistory.postValue(predictionLogs)

                    } else {
                        val errorBody = response.errorBody()
                        val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                        val message = responseJSON?.getString("message")
                        if (message != null) {
                            Log.d("Error Retrieving Data History", message)
                        }
                    }
                }

                override fun onFailure(call: Call<RiwayatDeteksiResponse>, t: Throwable) {
                }
            })
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getId(): String? {
        return tokenManager.getId()
    }

}