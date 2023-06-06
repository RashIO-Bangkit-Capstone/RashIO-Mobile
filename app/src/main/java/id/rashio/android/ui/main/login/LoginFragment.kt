package id.rashio.android.ui.main.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import id.rashio.android.R
import id.rashio.android.api.ApiConfig
import id.rashio.android.model.LoginRequest
import id.rashio.android.model.LoginResponse
import id.rashio.android.ui.main.homepage.HomeFragment
import id.rashio.android.ui.main.register.RegisterFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton: Button = view.findViewById(R.id.btn_login)
        loginButton.setOnClickListener {
            // Create and commit a fragment transaction to navigate to the HomeFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_login, HomeFragment())
            fragmentTransaction.commit()
        }

        val linkToRegister: TextView = view.findViewById(R.id.link_to_register)
        linkToRegister.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_login, RegisterFragment())
            fragmentTransaction.commit()
        }


        isLogin()

        val btnLogin = view.findViewById(R.id.btn_login) as Button
        btnLogin.setOnClickListener {
            val edt_mail = view.findViewById(R.id.edt_mail) as EditText
            val edt_psswrd = view.findViewById(R.id.edt_psswrd) as EditText

            val email = edt_mail.text.toString()
            val password = edt_psswrd.text.toString()

            login(email, password)
        }

        return view
    }

    companion object {

    }

    fun saveToken(accessToken :String, refreshToken :String ){
        val sharedPreferences = context?.getSharedPreferences("RashIO", 0)
        val editor = sharedPreferences?.edit()
        editor?.putString("ACCESS_TOKEN", accessToken)
        editor?.putString("REFRESH_TOKEN", refreshToken)
        editor?.apply()
    }

    fun goToHomeFragment() {
        val fragmentManager: FragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_login, HomeFragment())
            .commit()
    }

    private fun login(email :String, password :String){
        val apiService = ApiConfig.getApiService()

        val loginRequest = LoginRequest(email, password)

        val call = apiService.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val accessToken = loginResponse?.data?.accessToken.toString()
                    val refreshToken = loginResponse?.data?.refreshToken.toString()

                    saveToken(accessToken, refreshToken)
                    goToHomeFragment()

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun isLogin(){
        val sharedPreferences = context?.getSharedPreferences("RashIO", 0)
        val accessToken = sharedPreferences?.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPreferences?.getString("REFRESH_TOKEN", null)

        if (accessToken != null && refreshToken != null) {
            goToHomeFragment()
        }
    }
}