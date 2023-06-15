package id.rashio.android.model

data class RegisterResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: RegisterData,
)

data class RegisterData(
    val userId: String
)