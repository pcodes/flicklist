package io.patcody.flicklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    lateinit var firebaseAuth: FirebaseAuth

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_search -> {
                toolbar.title = getString(R.string.toolbar_search)
                val searchFragment = SearchFragment()
                openFragment(searchFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_list -> {
                toolbar.title = getString(R.string.toolbar_list)
                val listFragment = ListFragment()
                openFragment(listFragment)
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        firebaseAuth = FirebaseAuth.getInstance()
        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val email = firebaseAuth.currentUser?.email
        toolbar.title = getString(R.string.welcome_message, email)
        val searchFragment = SearchFragment()
        openFragment(searchFragment)
    }
}
