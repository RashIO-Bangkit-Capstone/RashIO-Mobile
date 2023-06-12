package id.rashio.android.ui.main.profile

import android.content.SharedPreferences
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

    fun logout(){
        val editor = sharedPreferences.edit()
        editor.remove("ACCESS_TOKEN")
        editor.remove("REFRESH_TOKEN")
        editor.apply()
    }
}