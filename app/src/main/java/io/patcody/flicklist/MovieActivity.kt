package io.patcody.flicklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MovieActivity : AppCompatActivity() {

    private lateinit var movie_id: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        movie_id = findViewById(R.id.movie_id)
        movie_id.text = intent.getStringExtra("MOVIE_ID")
    }
}
