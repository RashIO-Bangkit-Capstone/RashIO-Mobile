package id.rashio.android.ui.main.profile.bookmarksection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.local.entity.BookmarkArticleEntity
import id.rashio.android.data.repository.ArticleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkUiState(
    val error: String? = null,
    val article: List<BookmarkArticleEntity> = emptyList()
)

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val uiState: StateFlow<BookmarkUiState> =
        combine(_error, articleRepository.getAllBookmarkedArticle()) { error, articles ->
            BookmarkUiState(
                error = error,
                article = articles
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            initialValue = BookmarkUiState()
        )


    fun articleBookmarked(articleId: Int, title: String, imageUrl: String, author: String) {
        viewModelScope.launch {
            if (articleRepository.getArticleIsBookmarked(articleId)) {
                articleRepository.removeBookmarkedArticle(articleId)
            } else {
                articleRepository.articleBookmarked(articleId, title, imageUrl, author)
            }
        }
    }

    fun showError(msg: String) {
        _error.update {
            "Error while retrieving data, $msg"
        }
    }

    fun errorShown() {
        _error.update {
            null
        }
    }
}