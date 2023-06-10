package id.rashio.android.model

data class DetailArticle(
    val status: String,
    val code: Int,
    val message: String,
    val data: DataDetailArticle
)

data class DataDetailArticle(
    val id: Int,
    val title: String,
    val referenceUrl: String,
    val imageUrl: String,
    val author: String,
    val createdAt: String,
    val bodies: List<String>
)