package com.example.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * [RecyclerView.Adapter] that can display a list of [InTheatersMovie] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MovieInfo(
    private val movies: List<InTheatersMovie>,
    private val mListener: OnListFragmentInteractionListener
) : RecyclerView.Adapter<MovieInfo.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_improved, parent, false) // Use the correct layout
        return MovieViewHolder(view)
    }


    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMovieTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView
        val mMovieImage: ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView
        val mMovieDescription: TextView = mView.findViewById<View>(R.id.movie_description) as TextView

        override fun toString(): String {
            return mMovieTitle.toString()
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.mMovieTitle.text = movie.title
        holder.mMovieDescription.text = movie.overview ?: "No description available" // Set a default value if overview is null

        Glide.with(holder.mView)
            .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
            .centerInside()
            .into(holder.mMovieImage)

        holder.mView.setOnClickListener {
            mListener.onItemClick(movie)
        }
    }




    override fun getItemCount(): Int {
        return movies.size
    }
}

