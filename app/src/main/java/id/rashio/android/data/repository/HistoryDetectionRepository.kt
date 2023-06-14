package id.rashio.android.data.repository

import id.rashio.android.model.RiwayatDeteksiResponse
import retrofit2.Call

interface HistoryDetectionRepository {

    fun getAllHistory(id: String): Call<RiwayatDeteksiResponse>
}