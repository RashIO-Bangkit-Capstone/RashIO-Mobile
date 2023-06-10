package id.rashio.android.ui.main.detailartikel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.databinding.FragmentDetailArtikelBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class DetailArtikelFragment : Fragment() {

    private var _binding: FragmentDetailArtikelBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailArtikelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailArtikelBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navView.setupWithNavController(findNavController())

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is DetailArticleUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is DetailArticleUiState.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.tvTitleItem.text = uiState.article.data.title
                    val datePublished = uiState.article.data.createdAt
                    binding.tvDate.text = convertTimestampToFormattedDate(datePublished)
                    binding.tvAuthor.text = uiState.article.data.author
                    binding.tvReferenceLink.setOnClickListener {
                        val url = uiState.article.data.referenceUrl
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    val articleContentLayout = binding.articleContentLayout
                    for (paragraph in uiState.article.data.bodies) {
                        val paragraphTextView = TextView(requireContext())
                        paragraphTextView.text = (paragraph + "\n")
                        paragraphTextView.setTextColor(resources.getColor(android.R.color.black))
                        articleContentLayout.addView(paragraphTextView)
                    }
                    Glide.with(this)
                        .load(uiState.article.data.imageUrl)
                        .into(binding.imageArticle)
                }
                is DetailArticleUiState.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Snackbar.make(
                        requireView(),
                        uiState.message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.navView.selectedItemId = R.id.homeFragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToFormattedDate(timestamp: String): String {
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)

        val dateTime = LocalDateTime.parse(timestamp, inputFormatter)
        return outputFormatter.format(dateTime)
    }
}