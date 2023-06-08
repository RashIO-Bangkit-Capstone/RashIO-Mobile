package id.rashio.android.model

data class Article(
    val status: String,
    val code: Int,
    val message: String,
    val data: List<DataArticle>
)

data class DataArticle(
    val id: Int,
    val title: String,
    val referenceUrl: String,
    val imageUrl: String
)

