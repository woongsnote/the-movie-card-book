package com.woongsnote.mcb.pages.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woongsnote.mcb.data.model.Movie
import com.woongsnote.mcb.databinding.ItemListSearchBinding

class SearchAdapter(
    private var movies: List<Movie>, private var onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemListSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class Holder(private val binding: ItemListSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {

            Glide.with(itemView).load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .into(binding.ivPosterSearch)
            binding.tvTitleSearch.text = movie.title
            binding.tvDateSearch.text = movie.releaseDate

            itemView.setOnClickListener {
                onMovieClick.invoke(movie)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}