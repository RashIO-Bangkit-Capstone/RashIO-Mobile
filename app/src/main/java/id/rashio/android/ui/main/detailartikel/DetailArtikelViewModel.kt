package id.rashio.android.ui.main.detailartikel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.ArticleRepository
import id.rashio.android.model.DetailArticle
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
                    _uiState.postValue(
                        data?.let { DetailArticleUiState.Success(it) }
                    )

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


}

sealed interface DetailArticleUiState {
    object Loading : DetailArticleUiState
    data class Success(val article: DetailArticle) : DetailArticleUiState
    data class Error(val message: String) : DetailArticleUiState
}

