package id.rashio.android.ui.main.register

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
import id.rashio.android.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegis.setOnClickListener {
            val name = binding.rgstName.text.toString()
            val email = binding.rgstEmail.text.toString()
            val phoneNumber = binding.rgstNoHp.text.toString()
            val password = binding.rgstPsswrd.text.toString()
            val confirmPassword = binding.rgstConfirmPsswrd.text.toString()

            viewModel.register(name, email, phoneNumber, password, confirmPassword)
        }

        binding.linkToLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                if (it.navigateToLogin == true) {
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    viewModel.navigatedToLogin()
                }
                it.error?.let { message ->
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    viewModel.errorShown()
                }
            }
        }
    }
}