package id.rashio.android.model

data class LoginResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val accessToken: String,
    val refreshToken:String
)