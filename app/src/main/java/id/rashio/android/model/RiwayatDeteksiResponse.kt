package id.rashio.android.model

data class RiwayatDeteksiResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: DataRiwayatDeteksi
)

data class DataRiwayatDeteksi(
    val predictionLogs: List<PredictionLogs>
)

data class PredictionLogs(
    val imageUrl: String,
    val result: String,
    val percentage: Float,
    val createdAt: String
)