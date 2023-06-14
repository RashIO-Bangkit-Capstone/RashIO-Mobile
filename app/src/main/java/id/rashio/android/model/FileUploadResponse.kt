package id.rashio.android.model

data class FileUploadResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: ResultData
)

data class ResultData(
    val result: String,
    val percentage: Float,
    val imageUrl: String
)