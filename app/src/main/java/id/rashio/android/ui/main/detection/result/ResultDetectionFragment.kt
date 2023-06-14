package id.rashio.android.ui.main.detection.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.databinding.FragmentResultDetectionBinding

@AndroidEntryPoint
class ResultDetectionFragment : Fragment() {

    private var _binding: FragmentResultDetectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultDetectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResultDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navView.setupWithNavController(findNavController())

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ResultDetectionUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultDetectionUiState.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Snackbar.make(
                        requireView(),
                        uiState.message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is ResultDetectionUiState.Success -> {
                    binding.tvItemTitle.text = uiState.disease.data.name
                    val percentage = ((uiState.percentage ?: 0F).times(100)).toInt()
                    binding.tvPercentage.text = ("$percentage%")
                    binding.tvAccurationPercent.progress = percentage
                    val descriptionContentLayout = binding.descriptionContentLayout
                    for (paragraph in uiState.disease.data.descriptions) {
                        val paragraphTextView = TextView(requireContext())
                        paragraphTextView.text = (paragraph + "\n")
                        paragraphTextView.setTextColor(resources.getColor(android.R.color.black))
                        descriptionContentLayout.addView(paragraphTextView)
                    }
                    val medicationContentLayout = binding.medicationContentLayout
                    for (paragraph in uiState.disease.data.treatments) {
                        val paragraphTextView = TextView(requireContext())
                        paragraphTextView.text = (paragraph + "\n")
                        paragraphTextView.setTextColor(resources.getColor(android.R.color.black))
                        medicationContentLayout.addView(paragraphTextView)
                    }
                    Glide.with(requireContext())
                        .load(uiState.imageUrl)
                        .into(binding.ivDisease)
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.navView.selectedItemId = R.id.detectionFragment
    }
}