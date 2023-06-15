package id.rashio.android.data.repository

import id.rashio.android.model.FileUploadResponse
import retrofit2.Call
import java.io.File

interface DetectionRepository {

    fun uploadDetection(photo: File): Call<FileUploadResponse>

}