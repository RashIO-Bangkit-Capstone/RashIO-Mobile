package id.rashio.android.data.repository

import id.rashio.android.api.ApiService
import id.rashio.android.model.DiseaseResponse
import retrofit2.Call
import javax.inject.Inject

class DefaultDiseaseRepository @Inject constructor(
    private val api: ApiService
) : DiseaseRepository {

    override fun getDisease(name: String): Call<DiseaseResponse> {
        return api.getDiseases(name)
    }
}