package com.omdb.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omdb.app.databinding.ItemViewMovieBinding
import com.omdb.app.models.Search
import com.omdb.app.ui.custom.InfoChip
import com.omdb.app.util.loadImageFromUrl

class MoviesSearchAdapter : RecyclerView.Adapter<MoviesSearchAdapter.MovieSearchItemViewHolder>() {

    private val differItemCallback = object : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differItemCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieSearchItemViewHolder(
            ItemViewMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MovieSearchItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class MovieSearchItemViewHolder(private val itemViewBinding: ItemViewMovieBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(data: Search) = with(itemViewBinding.root) {
            itemViewBinding.apply {

                ivPoster.loadImageFromUrl(data.Poster?:"")

                tvMovieTitle.text = data.Title?: ""

                //remove all views to avoid duplication as it redraws
                chipsContainer.removeAllViews()

                data.Year?.let {
                    val yChip = InfoChip(chipsContainer.context)
                    yChip.text = it
                    chipsContainer.addView(yChip)
                }

                data.Type?.let {
                    val tChip = InfoChip(chipsContainer.context)
                    tChip.text = it
                    chipsContainer.addView(tChip)
                }

                //Dummy rating
                imdbRatingTv.text = "4.5"

                playButton.setOnClickListener {  }
            }
        }
    }

}