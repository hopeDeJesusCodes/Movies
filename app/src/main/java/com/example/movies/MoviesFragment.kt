package com.example.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import okhttp3.Headers
import org.json.JSONObject

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
        recyclerView.layoutManager = GridLayoutManager(context, 1)
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
                try {
                    val movies = parseTmdbResponse(json)
                    recyclerView.adapter = MovieInfo(movies, this@MoviesFragment)
                    progressBar.hide()
                } catch (e: Exception) {
                    Log.e("MoviesFragment", "Error parsing JSON: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                progressBar.hide()
                t?.message?.let {
                    Log.e("MoviesFragment", "HTTP Request Failed: $errorResponse")
                    Log.e("MoviesFragment", "Error Message: ${t.message}")
                }
            }
        }]
    }


    override fun onItemClick(item: InTheatersMovie) {
        Toast.makeText(context, "Movie: ${item.title}", Toast.LENGTH_LONG).show()
    }

    data class TmdbResponse(
        val results: List<InTheatersMovie>
    )

    private fun parseTmdbResponse(json: JsonHttpResponseHandler.JSON): List<InTheatersMovie> {
        val movieList = mutableListOf<InTheatersMovie>()

        try {
            val jsonString = json.toString() // Convert the JSON response to a string

            // Check if the string starts with "jsonObject="
            if (jsonString.startsWith("jsonObject=")) {
                // Remove the prefix to get the JSON data
                val jsonData = jsonString.substring("jsonObject=".length)

                // Create a JSON object from the JSON data
                val jsonObject = JSONObject(jsonData)

                // Extract the "results" array
                val resultsArray = jsonObject.getJSONArray("results")

                val gson = Gson()

                for (i in 0 until resultsArray.length()) {
                    val movieResponse = resultsArray.getJSONObject(i)
                    val title = movieResponse.getString("title")
                    val posterPath = movieResponse.getString("poster_path")
                    val overview = movieResponse.getString("overview") // Extract the overview

                    // Create an InTheatersMovie object for this movie
                    val movieInfo = InTheatersMovie(id = 0, title = title, overview = overview, posterPath = posterPath)


                    // Add the InTheatersMovie object to the list
                    movieList.add(movieInfo)
                }

                Log.d("MoviesFragment", "Number of movies: ${movieList.size}")
            } else {
                Log.e("MoviesFragment", "Invalid JSON format: $jsonString")
            }
        } catch (e: Exception) {
            Log.e("MoviesFragment", "Error parsing JSON: ${e.message}")
            Log.e("MoviesFragment", "JSON Response: ${json.toString()}")
        }

        // Return the list of InTheatersMovie objects
        return movieList
    }


}
