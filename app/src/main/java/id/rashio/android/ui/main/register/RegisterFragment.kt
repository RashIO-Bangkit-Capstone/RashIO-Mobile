package id.rashio.android.ui.main.register

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
import id.rashio.android.ui.main.login.LoginFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val regisButton: Button = view.findViewById(R.id.btn_regis)
        regisButton.setOnClickListener {
            // Create and commit a fragment transaction to navigate to the HomeFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_login, LoginFragment())
            fragmentTransaction.commit()
        }

        val linkToRegister: TextView = view.findViewById(R.id.link_to_login)
        linkToRegister.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_login, LoginFragment())
            fragmentTransaction.commit()
        }

        return view
    }

    companion object {
        // TODO: Rename and change types and number of parameters

    }
}