package io.patcody.flicklist


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        val userID = firebaseAuth.currentUser?.uid

        recyclerView = view.findViewById(R.id.list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val userMovies = mutableListOf<MovieResult>()
        val reference = firebaseDatabase.getReference("movies/$userID")

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

                recyclerView.adapter = MovieAdapter(userMovies, false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ListFragment.context, "Failed to retrieve user movies: $databaseError", Toast.LENGTH_LONG).show()
            }
        })


    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }


}
