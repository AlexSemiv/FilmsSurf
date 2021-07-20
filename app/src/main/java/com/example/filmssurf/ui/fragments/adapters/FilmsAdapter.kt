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
import okhttp3.internal.notify

class FilmsAdapter(
    private val idOfFavoriteFilms: List<Int>
): RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(
        val binding: ItemFilmBinding
    ): RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Film, newItem: Film) =
            oldItem.hashCode() == newItem.hashCode()
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
                .load(film.poster_path)
                .fitCenter()
                .error(R.drawable.ic_refresh)
                .into(holder.binding.ivFilm)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(film)
            }
        }

        holder.binding.cbFavorite.isChecked = idOfFavoriteFilms.contains(film.id)

        holder.binding.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                isCheckedCheckBoxListener?.let { insert ->
                    film.isFavorite = true
                    insert(film)
                }
            } else {
                isNotCheckedCheckBoxListener?.let { delete ->
                    film.isFavorite = false
                    delete(film)
                }
            }
        }
    }

    private var onItemClickListener: ((Film) -> Unit)? = null
    fun setOnItemCLickListener(listener: (Film) -> Unit){
        onItemClickListener = listener
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