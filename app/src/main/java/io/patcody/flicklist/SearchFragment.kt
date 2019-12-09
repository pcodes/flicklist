package io.patcody.flicklist


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    lateinit var searchText: TextView
    lateinit var searchButton: Button
    lateinit var firebaseDataBase: FirebaseDatabase
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchText = view.findViewById(R.id.text_search_box)
        searchButton = view.findViewById(R.id.search_button)

        firebaseDataBase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        //Get user id and get reference to user db
        val userID = firebaseAuth.currentUser?.uid
        val movies = mutableListOf<MovieResult>()

        recyclerView = view.findViewById(R.id.search_recycler_view)
        //val movieAdapter = MovieAdapter(movies, resources.getString(R.string.rapid_api_key))
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = MovieAdapter(movies, true)

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
                Toast.makeText(this@SearchFragment.context, "Failed to retrieve user movies: $databaseError", Toast.LENGTH_LONG).show()
            }
        })

        searchButton.setOnClickListener {
            activity.doAsync {
                val movieManager = MovieManager()
                movies.clear()

                try {
                    val movieResults = movieManager.searchForMovie(searchText.text.toString(), resources.getString(R.string.rapid_api_key))
                    activity!!.runOnUiThread {
                        for (movie in movieResults) {
                            val isMovieInUserList = userMovies.any {movieResult -> movieResult.id == movie.id}
                            movies.add(MovieResult(movie.name, movie.id, movie.posterUrl, isMovieInUserList))
                        }

                        recyclerView.adapter = MovieAdapter(movies, true)

                    }
                } catch (e: Exception) {
                    Log.wtf("okHTTP ERROR", e.toString())
                    activity!!.runOnUiThread {
                        Toast.makeText(activity, "Error making movie API call", Toast.LENGTH_SHORT)
                    }
                }

            }
        }
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}
