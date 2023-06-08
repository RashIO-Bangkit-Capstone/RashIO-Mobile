package id.rashio.android.ui.main.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)

        if (accessToken != null && refreshToken != null) {
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