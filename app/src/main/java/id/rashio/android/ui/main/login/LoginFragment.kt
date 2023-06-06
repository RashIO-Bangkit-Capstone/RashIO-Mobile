package id.rashio.android.ui.main.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import id.rashio.android.R
import id.rashio.android.ui.main.homepage.HomeFragment
import id.rashio.android.ui.main.register.RegisterFragment

class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        return view
    }

    companion object {

    }
}