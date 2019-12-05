package io.patcody.flicklist


import android.app.Activity
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

        //responseTextView = view.findViewById(R.id.movie_response_text)
        //addMovie = view.findViewById(R.id.addMovie)

        firebaseDataBase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        //Get user id and get reference to user db
        val userID = firebaseAuth.currentUser?.uid
        val movies = mutableListOf<MovieResult>()

        recyclerView = view.findViewById(R.id.recycler_view)
        val movieAdapter = MovieAdapter(movies)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = movieAdapter

        searchButton.setOnClickListener {
            activity.doAsync {
                val movieManager = MovieManager()

                try {
                    val movieResults = movieManager.searchForMovie(searchText.text.toString(), resources.getString(R.string.rapid_api_key))
                    activity!!.runOnUiThread {
                        for (movie in movieResults) {
                            movies.add(MovieResult(movie.name, movie.id, movie.posterUrl))
                        }

                        movieAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Log.wtf("okHTTP ERROR", e.toString())
                    activity!!.runOnUiThread {
                        Toast.makeText(activity, "Error making movie API call", Toast.LENGTH_SHORT)
                    }
                }

            }
        }

//        addMovie.setOnClickListener {
//            if (movies.isNotEmpty()) {
//                for(movie in movies) {
//                    val movieName = movie.name
//                    val reference = firebaseDataBase.getReference("movies/$userID/$movieName")
//                    reference.setValue(movie)
//                }
//            }
//        }

    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}
