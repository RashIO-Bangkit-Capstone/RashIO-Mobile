package id.rashio.android.ui.main.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.api.ApiService
import id.rashio.android.model.RegisterRequest
import id.rashio.android.model.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

data class RegisterUiState(
    val navigateToLogin: Boolean? = null,
    val error: String? = null,
    val messages: String? = null,
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,

        ) {
        val registerRequest = RegisterRequest(name, email, phoneNumber, password, confirmPassword)
        val call = api.register(registerRequest)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    val message = registerResponse?.status.toString()
                    showMsg(message)
                    navigateToLogin()

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    showError(message ?: "Unexpected Error")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            }
        })
    }

    fun navigateToLogin() {
        _uiState.update {
            it.copy(navigateToLogin = true)
        }
    }

    fun navigatedToLogin() {
        _uiState.update {
            it.copy(navigateToLogin = null)
        }
    }

    fun showError(msg: String) {
        _uiState.update {
            it.copy(error = "Register Failed, $msg")
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun showMsg(msg: String) {
        _uiState.update {
            it.copy(error = "Register $msg")
        }
    }


}