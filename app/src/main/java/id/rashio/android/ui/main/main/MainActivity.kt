package id.rashio.android.ui.main.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.databinding.ActivityMainBinding
import id.rashio.android.ui.main.homepage.HomeFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_navigation) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                if (it.navigateToLogin == true) {
                    navController.navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                    viewModel.navigatedToLogin()
                }
            }
        }
        viewModel.isLogin()

    }
}