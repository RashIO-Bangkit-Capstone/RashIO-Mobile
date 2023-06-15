package id.rashio.android.data.repository

import android.content.SharedPreferences
import id.rashio.android.api.ApiService
import id.rashio.android.model.RiwayatDeteksiResponse
import id.rashio.android.utils.TokenManager
import retrofit2.Call
import javax.inject.Inject

class DefaultHistoryDetectionRepository @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
) : HistoryDetectionRepository {

    private val tokenManager = TokenManager(sharedPreferences)

    override fun getAllHistory(id: String): Call<RiwayatDeteksiResponse> {
        val authorization = "Bearer ${tokenManager.getAccessToken()}"

        return api.historyDetection(authorization, id)
    }
}
