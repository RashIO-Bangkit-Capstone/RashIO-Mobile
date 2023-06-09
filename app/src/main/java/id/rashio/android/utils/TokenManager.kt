package id.rashio.android.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.util.Base64

class TokenManager(
        private val sharedPreferences: SharedPreferences
    ) {

    fun saveAccessToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", token)
        editor.apply()
    }

    fun saveRefreshToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("REFRESH_TOKEN", token)
        editor.apply()
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun saveNameFromToken(token: String){
//        val decoded = decodeToken(token)
//        val jsonObject = JSONObject(decoded)
//        val name = jsonObject.getString("name")
//        val editor = sharedPreferences.edit()
//        editor.putString("NAME", name)
//        editor.apply()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun saveIdFromToken(token: String){
//        val decoded = decodeToken(token)
//        val jsonObject = JSONObject(decoded)
//        val id = jsonObject.getString("id")
//        val editor = sharedPreferences.edit()
//        editor.putString("ID", id)
//        editor.apply()
//    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("REFRESH_TOKEN", null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getName(): String? {
        return if (decodeToken() != "") {
            val jsonObject = JSONObject(decodeToken())
            jsonObject.getString("name")
        } else {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getId(): String? {
        return if (decodeToken() != "") {
            val jsonObject = JSONObject(decodeToken())
            jsonObject.getString("id")
        } else {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decodeToken(): String {
        val token = getAccessToken()
        val body = token?.split(".")?.get(1)
        return if (body != null) {
            val decoded = Base64.getDecoder().decode(body)
            String(decoded)
        } else {
            ""
        }
    }
}