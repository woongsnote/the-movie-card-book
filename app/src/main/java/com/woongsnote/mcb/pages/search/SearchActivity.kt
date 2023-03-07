package com.woongsnote.mcb.pages.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.data.api.movie.MoviesRepository
import com.woongsnote.mcb.data.model.Movie
import com.woongsnote.mcb.data.model.Review
import com.woongsnote.mcb.databinding.ActivitySearchBinding
import com.woongsnote.mcb.pages.detail.DetailActivity

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var searchKeyWord = ""
    private lateinit var searchMoviesAdapter: SearchAdapter
    private var searchList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.etSearchTitle.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchKeyWord = binding.etSearchTitle.text.toString()
                    if (searchKeyWord.isEmpty()) {
                        binding.textInputLayout.error = "내용이 입력되지 않았어요!!"
                    } else {
                        binding.textInputLayout.error = null
//                        binding.etSearchTitle.clearFocus()
                        hideKeyboard()
                        getSearchMovies()

                        binding.pbSearch.visibility = View.GONE
                    }
                    return true
                }
                return false
            }
        })
        searchMoviesAdapter = SearchAdapter(searchList) { movie ->
            showMovieDetail(movie)
        }
        binding.rvSearchMovies.adapter = searchMoviesAdapter
        binding.rvSearchMovies.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

    }

    private fun getSearchMovies() {

        binding.pbSearch.visibility = View.VISIBLE
        MoviesRepository.getSearchMovies(
            searchKeyWord,
            ::onSearchMoviesFetched,
            ::onError
        )
    }

    private fun onSearchMoviesFetched(movies: List<Movie>) {
        searchMoviesAdapter.updateMovies(movies)
    }

    private fun onError() {
        Toast.makeText(this, "검색에 실패했습니다!", Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetail(movie: Movie) {

        val title = movie.title

        val docRef = db.collection(auth.currentUser!!.uid).document(title)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val intent = Intent(this, DetailActivity::class.java)
                val doc = document.data
                if (doc != null) {
                    intent.putExtra(
                        "REVIEW", Review(
                            doc["backPath"].toString(),
                            doc["posterPath"].toString(),
                            doc["title"].toString(),
                            doc["releaseDate"].toString(),
                            doc["overView"].toString(),
                            doc["rating"] as Double?,
                            doc["viewDate"].toString(),
                            doc["comment"].toString(),
                        )
                    )
                    startActivity(intent)
                } else {
                    intent.putExtra(
                        "REVIEW", Review(
                            movie.backPath,
                            movie.posterPath,
                            movie.title,
                            movie.releaseDate,
                            movie.overview,
                            0.0, "", ""
                        )
                    )
                    startActivity(intent)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearchTitle.windowToken, 0)
    }
}