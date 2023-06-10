package id.rashio.android.model

data class DiseaseResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: DataDisease
)

data class DataDisease(
    val id: String,
    val name: String,
    val descriptions: List<String>,
    val treatments: List<String>
)