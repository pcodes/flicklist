package io.patcody.flicklist

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import javax.net.ssl.HostnameVerifier

class MovieManager {
    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        builder.addInterceptor(logging)

        //Need to ignore the unverified SSL certificate problem
        builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        okHttpClient = builder.build()
    }

    fun searchForMovie(searchTerm: String, apiKey: String): List<MovieResult> {
        val movieResultsList: MutableList<MovieResult> = mutableListOf()
        val searchUrl: String = "https://www.movie-database-imdb-alternative.p.rapidapi.com/"
        val searchParams: String = "?s=$searchTerm"

        val request = Request.Builder()
            .url(searchUrl + searchParams)
            .header("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
            .header("x-rapidapi-key", apiKey)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseString: String?  = response.body?.string()

        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            val jsonConversion = JSONObject(responseString)
            if (!jsonConversion.getBoolean("Response")) {
                return movieResultsList
            }

            val movieResultsJSON = jsonConversion.getJSONArray("Search")
            for (i in 0 until movieResultsJSON.length()) {
                val individualMovieJSON = movieResultsJSON.getJSONObject(i)
                val movieTitle = individualMovieJSON.getString("Title")
                val id = individualMovieJSON.getString("imdbID")
                val poster = individualMovieJSON.getString("Poster")

                val movie = MovieResult(
                    name = movieTitle,
                    id = id,
                    posterUrl = poster,
                    inList = false
                )

                if (!movieResultsList.any { it.id == movie.id }) {
                    movieResultsList.add(movie)
                }
            }
        }

        return movieResultsList
    }

    fun getMovieData(movieId: String, apiKey: String) : MovieItem? {
        val searchUrl: String = "https://movie-database-imdb-alternative.p.rapidapi.com/"
        val searchParams: String = "?i=$movieId"

        val request = Request.Builder()
            .url(searchUrl + searchParams)
            .header("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
            .header("x-rapidapi-key", apiKey)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseString: String? = response.body?.string()

        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            val jsonConversion = JSONObject(responseString)
            if (!jsonConversion.getBoolean("Response")) {
                return null
            }

            val movieTitle = jsonConversion.getString("Title")
            val year = jsonConversion.getString("Year")
            val rating = jsonConversion.getString("Rated")
            val releaseDate = jsonConversion.getString("Released")
            val director = jsonConversion.getString("Director")
            val plot = jsonConversion.getString("Plot")
            val imdbRating = jsonConversion.getString("imdbRating")
            val posterUrl = jsonConversion.getString("Poster")

            return MovieItem(movieId, movieTitle, year, rating, releaseDate, director, plot, imdbRating, posterUrl)
        }

        return null
    }
}