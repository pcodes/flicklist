package io.patcody.flicklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.doAsync

class MenuActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_search -> {
                toolbar.title = "MovieResult Search"
                val searchFragment = SearchFragment()
                openFragment(searchFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_list -> {
                toolbar.title = "MovieResult List"
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

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        toolbar.title = "MovieResult Search"
        val searchFragment = SearchFragment()
        openFragment(searchFragment)

        doAsync {
            runOnUiThread {

            }
        }

        
        
    }
}
