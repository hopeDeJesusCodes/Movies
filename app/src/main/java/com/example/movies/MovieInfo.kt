package com.example.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject // Example import for Gson


/**
 * [RecyclerView.Adapter] that can display a [InTheatersMovie] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MovieInfo(
    private val movieList: List<InTheatersMovie>, // Change the parameter type to List<InTheatersMovie>
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MovieInfo.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMovieTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView
        val mMovieImage: ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView

        override fun toString(): String {
            return mMovieTitle.toString()
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]

        holder.mMovieTitle.text = movie.title

        Glide.with(holder.mView)
            .load(movie.posterPath)
            .centerInside()
            .into(holder.mMovieImage)

        holder.mView.setOnClickListener {
            mListener?.onItemClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}