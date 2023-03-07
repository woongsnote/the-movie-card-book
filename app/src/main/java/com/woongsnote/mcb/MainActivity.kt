package com.woongsnote.mcb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.data.model.Review
import com.woongsnote.mcb.databinding.ActivityMainBinding
import com.woongsnote.mcb.pages.detail.DetailActivity
import com.woongsnote.mcb.pages.home.HomeAdapter
import com.woongsnote.mcb.pages.search.SearchActivity
import com.woongsnote.mcb.pages.user.UserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var uid: String? = null
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var homeAdapter: HomeAdapter
    private var homeList = arrayListOf<Review>()
    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        uid = auth.currentUser?.uid
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeList.clear()
        getReviews()
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun getReviews() {
        val ref: CollectionReference = db.collection(uid!!)
        ref.orderBy("viewDate").get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                binding.pbLoading.visibility = View.GONE
            } else {
                binding.pbLoading.visibility = View.VISIBLE
                binding.tvIntro.text = "영화 정보를 불러오는 중입니다!"
                for (document in result) {
                    val item = Review(
                        document["backPath"] as String,
                        document["posterPath"] as String,
                        document["title"] as String,
                        document["releaseDate"] as String,
                        document["overView"] as String,
                        document["rating"] as Double,
                        document["viewDate"] as String,
                        document["comment"] as String,
                    )
                    homeList.add(item)
                }
                showReviews()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "정보 로딩  실패!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {

        binding.pbLoading.visibility = View.GONE
        homeAdapter = HomeAdapter(homeList) { movie ->
            showDetail(movie)
        }
        binding.rvMovieListMain.adapter = homeAdapter

        binding.fabAddMovie.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.account -> {
                    showUser()
                    true
                }
                else -> false
            }
        }
    }

    private fun showReviews() {
        binding.pbLoading.visibility = View.GONE
        binding.tvIntro.visibility = View.GONE
        homeAdapter.updateReviews(homeList)
    }

    private fun showDetail(movie: Review) {

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("KEY", "Main")
        intent.putExtra(
            "REVIEW", Review(
                movie.back,
                movie.poster,
                movie.title,
                movie.releaseDate,
                movie.overview,
                movie.rating,
                movie.viewDate,
                movie.comment
            )
        )
        startActivity(intent)
    }

    private fun showUser() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500){
            waitTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 버튼을 한번 더 누르면 종료됩니다!",Toast.LENGTH_SHORT).show()
        }else{
            finish()
        }
    }
}