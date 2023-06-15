package id.rashio.android.ui.main.profile.bookmarksection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.adapter.BookmarkArticleAdapter
import id.rashio.android.databinding.FragmentBookmarkBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BookmarkArticleAdapter(onClick = { articleId ->
            Toast.makeText(requireContext(), "Still under development", Toast.LENGTH_SHORT).show()
        }, onBookmark = { articleId, title, imageUrl, author ->
            viewModel.articleBookmarked(articleId, title, imageUrl, author)
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookmark.layoutManager = layoutManager
        binding.rvBookmark.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                adapter.submitList(it.article)
            }
        }
    }

}