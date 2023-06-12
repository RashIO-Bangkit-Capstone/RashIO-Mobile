package id.rashio.android.ui.main.detailartikel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.ArticleRepository
import id.rashio.android.model.DataDetailArticle
import id.rashio.android.model.DetailArticle
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailArtikelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val articleId: Int? = savedStateHandle["articleId"]

    private val _uiState = MutableLiveData<DetailArticleUiState>()
    val uiState: LiveData<DetailArticleUiState> = _uiState

    init {
        if (articleId != null) {
            getArticleDetail(articleId)
        }
    }

    private fun getArticleDetail(articleId: Int) {
        val call = articleRepository.getArticleDetail(articleId)
        call.enqueue(object : Callback<DetailArticle> {
            override fun onResponse(
                call: Call<DetailArticle>,
                response: Response<DetailArticle>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    viewModelScope.launch {
                        _uiState.postValue(
                            data?.let {
                                val article = it.data
                                val isBookmarked =
                                    articleRepository.getArticleIsBookmarked(article.id)
                                DetailArticleUiState.Success(
                                    article.copy(isBookmarked = isBookmarked)
                                )
                            }
                        )
                    }

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    _uiState.postValue(
                        message?.let { DetailArticleUiState.Error(it) }
                    )
                }
            }

            override fun onFailure(call: Call<DetailArticle>, t: Throwable) {

            }
        })
    }

    fun articleBookmarked(articleId: Int) {
        viewModelScope.launch {
            if (articleRepository.getArticleIsBookmarked(articleId)) {
                articleRepository.removeBookmarkedArticle(articleId)
            } else {
                articleRepository.articleBookmarked(articleId)
            }
        }
    }


}

sealed interface DetailArticleUiState {
    object Loading : DetailArticleUiState
    data class Success(val article: DataDetailArticle) : DetailArticleUiState
    data class Error(val message: String) : DetailArticleUiState
}

