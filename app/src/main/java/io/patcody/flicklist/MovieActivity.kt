package io.patcody.flicklist

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import okhttp3.internal.notifyAll
import org.jetbrains.anko.doAsync
import java.lang.Exception

class MovieActivity : AppCompatActivity() {

    private lateinit var posterImage: ImageView
    private lateinit var movieLabel: TextView
    private lateinit var movieRelease: TextView
    private lateinit var imdbScore: TextView
    private lateinit var ageRating: TextView
    private lateinit var directorName: TextView
    private lateinit var plotSummary: TextView
    private lateinit var actionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieId = intent.getStringExtra("MOVIE_ID")

        val firebaseDataBase = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val userID = firebaseAuth.currentUser?.uid

        posterImage = findViewById(R.id.full_movie_poster)
        movieLabel = findViewById(R.id.full_movie_label)
        movieRelease = findViewById(R.id.movie_release_text)
        imdbScore = findViewById(R.id.imdb_score_text)
        ageRating = findViewById(R.id.age_rating_text)
        directorName = findViewById(R.id.director_text)
        plotSummary = findViewById(R.id.plot_summary_text)

        actionButton = findViewById(R.id.movie_view_button)

        val userMovies = mutableListOf<MovieResult>()
        val reference = firebaseDataBase.getReference("movies/$userID")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userMovies.clear()
                Log.wtf("FIREBASE", "Getting movies")
                dataSnapshot.children.forEach { child ->
                    val movie = child.getValue(MovieResult::class.java)
                    if (movie != null) {
                        userMovies.add(movie)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MovieActivity, "Failed to retrieve user movies: $databaseError", Toast.LENGTH_LONG).show()
            }
        })

        doAsync {
            val movieManager = MovieManager()

            try {
                val movieData = movieManager.getMovieData(movieId, getString(R.string.rapid_api_key))
                if (movieData != null) {
                    runOnUiThread {
                        Picasso.get().load(movieData.posterURL).into(posterImage)

                        movieLabel.text = movieData.movieTitle
                        movieRelease.text = getString(R.string.release_date, movieData.releaseDate)
                        imdbScore.text = getString(R.string.imdb_score, movieData.imdbRating)
                        ageRating.text = getString(R.string.age_rating_text, movieData.rating)
                        directorName.text = getString(R.string.director_text, movieData.director)
                        plotSummary.text = movieData.plot

                        //Set up the action button
                        val isAlreadyInUserList = userMovies.any {movieResult -> movieResult.id == movieData.id}
                        val movieRef = firebaseDataBase.getReference("movies/$userID/${movieData.id}")
                        if (isAlreadyInUserList) {
                            actionButton.text = getString(R.string.watched_text)
                            actionButton.setOnClickListener {
                                movieRef.removeValue()
                                actionButton.isEnabled = false
                            }
                        } else {
                            actionButton.text = getString(R.string.add_to_movie_list)
                            actionButton.setOnClickListener {
                                val movieEntry = MovieResult(movieData.movieTitle, movieData.id, movieData.posterURL, false)
                                movieRef.setValue(movieEntry)
                                actionButton.isEnabled = false
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.wtf("okHTTP ERROR", e.toString())
                runOnUiThread {
                    Toast.makeText(this@MovieActivity, "Error making movie API call", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
