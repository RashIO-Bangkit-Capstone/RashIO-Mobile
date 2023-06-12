package id.rashio.android.ui.main.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import id.rashio.android.api.ApiService
import id.rashio.android.utils.TokenManager
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val tokenManager = TokenManager(sharedPreferences)

}