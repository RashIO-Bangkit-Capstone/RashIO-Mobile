package id.rashio.android.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String
)