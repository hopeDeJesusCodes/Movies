package com.example.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/now_playing"

class MoviesFragment : Fragment(), OnListFragmentInteractionListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_movie, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // Using the client, perform the HTTP request
        client[TMDB_BASE_URL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                val movies = parseTmdbResponse(json)
                recyclerView.adapter = MovieInfo(movies, this@MoviesFragment)
                progressBar.hide()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                progressBar.hide()
                t?.message?.let {
                    Log.e("MoviesFragment", errorResponse)
                }
            }
        }]
    }

    override fun onItemClick(item: InTheatersMovie) {
        Toast.makeText(context, "Movie: ${item.title}", Toast.LENGTH_LONG).show()
    }

    private fun parseTmdbResponse(json: JsonHttpResponseHandler.JSON): List<InTheatersMovie> {
        val resultsArray = json.jsonArray.optJSONArray("results")
        val movies = mutableListOf<InTheatersMovie>()

        if (resultsArray != null) {
            for (i in 0 until resultsArray.length()) {
                val movieObject = resultsArray.getJSONObject(i)

                // Extract the title and poster_path from the movieObject
                val title = movieObject.optString("title")
                val posterPath = movieObject.optString("poster_path")

                // Create an InTheatersMovie object with the extracted data
                // Use the index i as a unique ID, since we don't have real IDs
                val movieInfo = InTheatersMovie(i, title, "Dummy Overview", posterPath)

                // Add the InTheatersMovie object to the list of movies
                movies.add(movieInfo)
            }
        }

        return movies
    }


}
