package com.woongsnote.mcb.pages.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woongsnote.mcb.MainActivity
import com.woongsnote.mcb.R
import com.woongsnote.mcb.data.model.Review
import com.woongsnote.mcb.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private var receiveData: Review? = null

    private var score = 0.0
    private var viewDate = ""
    private var comment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiveData = intent.getParcelableExtra("REVIEW")
        initViews(receiveData)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initViews(movieData: Review?) {

        if (movieData != null) {

            Glide.with(this).load("https://image.tmdb.org/t/p/w1280${movieData.back}")
                .into(binding.layoutMovieDetail.ivBackDetail)

            Glide.with(this).load("https://image.tmdb.org/t/p/w342${movieData.poster}")
                .into(binding.layoutMovieDetail.ivPosterDetail)

            binding.layoutMovieDetail.tvTitleDetail.text = movieData.title
            binding.layoutMovieDetail.tvOpenDateDetail.text = movieData.releaseDate
            binding.layoutMovieDetail.tvDescDetail.text = movieData.overview


            if (movieData.rating == 0.0) {
                binding.layoutReviewDetail.btnDp.text = getString(R.string.sel_date)
            } else {
                viewDate = movieData.viewDate.toString()
                binding.layoutReviewDetail.btnDp.text = viewDate
            }
            score = movieData.rating!!
            binding.layoutReviewDetail.rbScore.rating = (score / 2).toFloat()
            binding.layoutReviewDetail.tvScore.text = "${score.toFloat()} / 10.0"

            comment = movieData.comment.toString()
            binding.layoutReviewDetail.etReview.setText(comment)

        } else {
            Toast.makeText(this, "정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.layoutReviewDetail.btnDp.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.sel_date_title))
                    .build()

            datePicker.show(supportFragmentManager, "tag")

            datePicker.addOnPositiveButtonClickListener {

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                viewDate = sdf.format(datePicker.selection)
                binding.layoutReviewDetail.btnDp.text = viewDate
            }
        }

        binding.layoutReviewDetail.rbScore.setOnRatingBarChangeListener { _, rating, _ ->
            score = (rating * 2).toDouble()
            binding.layoutReviewDetail.tvScore.text = "$score / 10.0"
        }

        binding.layoutReviewDetail.btnSaveReview.setOnClickListener {
            saveReview(receiveData!!)
        }

        binding.layoutReviewDetail.btnCancelReview.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveReview(review: Review) {

        val date = binding.layoutReviewDetail.btnDp.text.toString()
        val comment = binding.layoutReviewDetail.etReview.text.toString()

        if ((date == "날짜 선택" || date == "") || (comment == "") || (score == 0.0)) {
            Snackbar.make(binding.root, "아직 작성하지 않은 곳이 있습니다!", Snackbar.LENGTH_SHORT).show()
        } else {
            val docTitle = review.title

            val result = hashMapOf(
                "backPath" to review.back,
                "posterPath" to review.poster,
                "title" to docTitle,
                "releaseDate" to review.releaseDate,
                "overView" to review.overview,
                "rating" to score,
                "viewDate" to viewDate,
                "comment" to comment,
            )

            val docRef = db.collection(auth.currentUser!!.uid)
            docRef.document(docTitle.toString()).set(result, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Saving Failed, Retry", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
