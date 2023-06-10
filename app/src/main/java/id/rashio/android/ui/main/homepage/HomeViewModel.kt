package id.rashio.android.ui.main.homepage

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rashio.android.data.repository.ArticleRepository
import id.rashio.android.model.Article
import id.rashio.android.model.DataArticle
import id.rashio.android.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

data class HomeUiState(
    val error: String? = null,
    val article: List<DataArticle> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val tokenManager = TokenManager(sharedPreferences)

    init {
        val call = articleRepository.getAllArticle()
        call.enqueue(object : Callback<Article> {
            override fun onResponse(
                call: Call<Article>,
                response: Response<Article>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _uiState.update { HomeUiState(article = data) }

                } else {
                    val errorBody = response.errorBody()
                    val responseJSON = errorBody?.string()?.let { JSONObject(it) }
                    val message = responseJSON?.getString("message")
                    showError(message ?: "Unexpected Error")
                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
            }
        })
    }

    fun showError(msg: String) {
        _uiState.update {
            it.copy(error = "Error while retrieving data, $msg")
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserName(): String? {
        return tokenManager.getName()
    }
}