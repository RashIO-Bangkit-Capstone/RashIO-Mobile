package id.rashio.android.ui.main.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MainUiState(
    val navigateToLogin: Boolean? = null,
    val error: String? = null
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun isLogin() {
        val tokenManager = TokenManager(sharedPreferences)

        if (tokenManager.getAccessToken() == null &&
            tokenManager.getRefreshToken() == null) {
            navigateToLogin()
        }
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
}