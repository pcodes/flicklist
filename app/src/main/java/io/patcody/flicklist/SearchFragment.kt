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
import org.jetbrains.anko.doAsync
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    lateinit var searchText: TextView
    lateinit var searchButton: Button
    lateinit var responseTextView: TextView

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
        responseTextView = view.findViewById(R.id.movie_response_text)

        searchButton.setOnClickListener {
            activity.doAsync {
                val movieManager = MovieManager()

                try {
                    val movieResults = movieManager.searchForMovie(searchText.text.toString(), resources.getString(R.string.rapid_api_key))
                    activity!!.runOnUiThread {
                        responseTextView.text = ""

                        for (movie in movieResults) {
                            responseTextView.text = responseTextView.text.toString() + movie.name + "\n"
                        }
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
