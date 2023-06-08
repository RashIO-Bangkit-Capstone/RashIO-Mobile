package id.rashio.android.ui.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root


//
//
//        isLogin()
//
//        val btnLogin = view.findViewById(R.id.btn_login) as Button
//        btnLogin.setOnClickListener {
//            val edt_mail = view.findViewById(R.id.edt_mail) as EditText
//            val edt_psswrd = view.findViewById(R.id.edt_psswrd) as EditText
//
//            val email = edt_mail.text.toString()
//            val password = edt_psswrd.text.toString()
//
//            login(email, password)
//        }
//
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.edtMail.text.toString()
            val password = binding.edtPsswrd.text.toString()
            viewModel.login(email, password)
        }

        binding.linkToRegister.setOnClickListener() {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                if (it.navigateToHome == true) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    viewModel.navigatedToHome()
                }
                it.error?.let { message ->
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    viewModel.errorShown()
                }
            }
        }
    }


}