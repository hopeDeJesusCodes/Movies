package com.example.movies

/**
 * This interface is used by the [MovieInfo] to ensure
 * it has an appropriate Listener.
 *
 * In this app, it's implemented by [MoviesFragment]
 */
interface OnListFragmentInteractionListener {
    fun onItemClick(item: InTheatersMovie)
}
