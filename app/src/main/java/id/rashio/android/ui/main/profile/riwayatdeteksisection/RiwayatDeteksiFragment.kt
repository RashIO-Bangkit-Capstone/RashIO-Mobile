package id.rashio.android.ui.main.profile.riwayatdeteksisection

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.adapter.HistoryDetectionAdapter
import id.rashio.android.databinding.FragmentRiwayatDeteksiBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RiwayatDeteksiFragment : Fragment() {

    private var _binding: FragmentRiwayatDeteksiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RiwayatDeteksiViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatDeteksiBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryDetectionAdapter(onClick = { result, percentage ->
            Toast.makeText(requireContext(), "Still under development", Toast.LENGTH_SHORT).show()
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.adapter = adapter

        lifecycleScope.launch {
            viewModel.allHistory.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        viewModel.getHistory()
    }

}
