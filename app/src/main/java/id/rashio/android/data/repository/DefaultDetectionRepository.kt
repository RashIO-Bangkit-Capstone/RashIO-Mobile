package id.rashio.android.data.repository

import android.content.SharedPreferences
import id.rashio.android.api.ApiService
import id.rashio.android.model.FileUploadResponse
import id.rashio.android.utils.TokenManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File
import javax.inject.Inject

class DefaultDetectionRepository @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
) : DetectionRepository {

    private val tokenManager = TokenManager(sharedPreferences)

    override fun uploadDetection(photo: File): Call<FileUploadResponse> {
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            photo.name,
            requestImageFile
        )

        val authorization = "Bearer ${tokenManager.getAccessToken()}"

        return api.uploadImage(authorization, imageMultipart)
    }
}