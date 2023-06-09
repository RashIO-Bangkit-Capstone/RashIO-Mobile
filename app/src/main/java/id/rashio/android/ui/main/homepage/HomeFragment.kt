package id.rashio.android.ui.main.homepage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.adapter.ArticleListAdapter
import id.rashio.android.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navView.setupWithNavController(findNavController())


        val textDescription = binding.textDescription
        val name = viewModel.getUserName()
        val greeting = getString(R.string.greetings, name)
        textDescription.text = greeting

        val adapter = ArticleListAdapter(onClick = { articleId ->

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBand.layoutManager = layoutManager
        binding.rvBand.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                adapter.submitList(it.article)
            }
        }


    }
}