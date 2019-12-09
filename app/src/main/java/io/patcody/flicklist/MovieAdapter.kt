package io.patcody.flicklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import java.lang.Exception

class MovieAdapter(val movies: List<MovieResult>, val showAddButton: Boolean) : RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_movie, parent, false)


        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val fireBaseDB = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        //Get user id and get reference to user db
        val userID = firebaseAuth.currentUser?.uid

        val currentMovie = movies[position]
        val ref = fireBaseDB.getReference("movies/$userID/${movies[position].id}")

        holder.movieTitle.text = currentMovie.name
        Picasso.get().load(currentMovie.posterUrl).into(holder.moviePoster)

        holder.addToListButton.setOnClickListener {
            try {
                ref.setValue(movies[position])
                movies[position].inList = true
                holder.addToListButton.isEnabled = false
            } catch (e: Exception) {
                Log.wtf("okHTTP ERROR", e.toString())
            }
        }

        if (movies[position].inList) {
            holder.addToListButton.isEnabled = false
        }

        if (!showAddButton) {
            holder.addToListButton.visibility = View.GONE
        }
    }

    class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moviePoster: ImageView = view.findViewById(R.id.movie_poster)
        val movieTitle: TextView = view.findViewById(R.id.movie_title)
        val addToListButton: Button = view.findViewById(R.id.add_to_list_button)
    }
}