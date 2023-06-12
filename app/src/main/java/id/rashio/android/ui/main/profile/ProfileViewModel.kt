package id.rashio.android.ui.main.profile

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.api.ApiService
import id.rashio.android.utils.TokenManager
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ApiService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val tokenManager = TokenManager(sharedPreferences)


    fun logout(){
        val editor = sharedPreferences.edit()
        editor.remove("ACCESS_TOKEN")
        editor.remove("REFRESH_TOKEN")
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserName(): String? {
        return tokenManager.getName()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEmail(): String? {
        return tokenManager.getEmail()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPhoneNumber(): String? {
        return tokenManager.getPhoneNumber()
    }
}