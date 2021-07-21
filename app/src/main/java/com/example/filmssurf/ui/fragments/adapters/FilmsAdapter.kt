package com.example.filmssurf.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmssurf.R
import com.example.filmssurf.databinding.ItemFilmBinding
import com.example.filmssurf.db.Film
import com.example.filmssurf.other.Utils.POSTER_SIZE
import okhttp3.internal.notify

class FilmsAdapter(): RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(
        val binding: ItemFilmBinding
    ): RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Film, newItem: Film) =
            oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilmViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_film,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = differ.currentList[position]

        holder.binding.film = film

        holder.itemView.apply {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/${POSTER_SIZE}${film.poster_path}")
                .override(120,180)
                .fitCenter()
                .placeholder(R.drawable.ic_downloading)
                .error(R.drawable.ic_error_downloading)
                .into(holder.binding.ivFilm)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(film)
            }
        }

        isFavoriteFilmListener?.let { isFavorite ->
            holder.binding.cbFavorite.isChecked = isFavorite(film)
            film.isFavorite = isFavorite(film)
        }

        holder.binding.cbFavorite.setOnClickListener {
            if(!film.isFavorite){
                isCheckedCheckBoxListener?.let { insert ->
                    insert(film)
                }
            } else {
                isNotCheckedCheckBoxListener?.let { delete ->
                    delete(film)
                }
            }
        }
    }

    private var onItemClickListener: ((Film) -> Unit)? = null
    fun setOnItemCLickListener(listener: (Film) -> Unit){
        onItemClickListener = listener
    }

    private var isFavoriteFilmListener: ((Film) -> Boolean)? = null
    fun setOnFavoriteFilmListener(listener: (Film) -> Boolean) {
        isFavoriteFilmListener = listener
    }

    private var isCheckedCheckBoxListener: ((Film) -> Unit)? = null
    fun setOnIsCheckedStateChangeListener(listener: (Film) -> Unit) {
        isCheckedCheckBoxListener = listener
    }

    private var isNotCheckedCheckBoxListener: ((Film) -> Unit)? = null
    fun setOnIsNotCheckedStateChangeListener(listener: (Film) -> Unit) {
        isNotCheckedCheckBoxListener = listener
    }
}