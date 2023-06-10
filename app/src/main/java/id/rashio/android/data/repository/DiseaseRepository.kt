package id.rashio.android.data.repository

import id.rashio.android.model.DiseaseResponse
import retrofit2.Call

interface DiseaseRepository {

    fun getDisease(name: String): Call<DiseaseResponse>
}