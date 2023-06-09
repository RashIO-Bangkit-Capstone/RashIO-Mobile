package id.rashio.android.ui.main.login

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.R
import id.rashio.android.api.ApiConfig
import id.rashio.android.api.ApiService
import id.rashio.android.model.LoginRequest
import id.rashio.android.model.LoginResponse
import id.rashio.android.ui.main.homepage.HomeFragment
import id.rashio.android.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

data class LoginUiState(
    val navigateToHome: Boolean? = null,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val tokenManager = TokenManager(sharedPreferences)

    fun login(email :String, password :String){
        val loginRequest = LoginRequest(email, password)
        val call = api.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val accessToken = loginResponse?.data?.accessToken.toString()
                    val refreshToken = loginResponse?.data?.refreshToken.toString()

                    val message = loginResponse?.status.toString()
                    showMsg(message ?: "success")
                    saveToken(accessToken, refreshToken)
                    navigateToHome()

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    showError(message?:"Unexpected Error")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }



    fun saveToken(accessToken :String, refreshToken :String ){
        tokenManager.saveAccessToken(accessToken)
        tokenManager.saveRefreshToken(refreshToken)
    }

    fun navigateToHome() {
        _uiState.update {
            it.copy(navigateToHome = true)
        }
    }

    fun navigatedToHome() {
        _uiState.update {
            it.copy(navigateToHome = null)
        }
    }

    fun showError(msg: String) {
        _uiState.update {
            it.copy(error = "Login Failed, $msg")
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }
    fun showMsg(msg: String) {
        _uiState.update {
            it.copy(error = "Login $msg, Welcome")
        }
    }

    fun msgShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }
}