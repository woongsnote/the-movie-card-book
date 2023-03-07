package com.woongsnote.mcb.pages.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woongsnote.mcb.data.model.Review
import com.woongsnote.mcb.databinding.ItemCardMainBinding

class HomeAdapter(private var reviews: List<Review>, private var onMovieClick: (movie: Review) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCardMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class Holder(private val binding: ItemCardMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {

            Glide.with(itemView).load("https://image.tmdb.org/t/p/w342${review.poster}")
                .into(binding.ivMainPoster)

            itemView.setOnClickListener {
                onMovieClick.invoke(review)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReviews(reviews: List<Review>){
        this.reviews = reviews
        notifyDataSetChanged()
    }
}