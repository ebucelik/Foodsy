package ebucelik.keepeasy.foodsy.home

import android.R.id.tabs
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.mealDetail.MealDetailActivity


class HomeActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var searchFragment: SearchFragment
    lateinit var sellFragment: SellFragment
    lateinit var accountFragment: AccountFragment
    lateinit var tabLayout: TabLayout

    companion object{
        const val USERNAME = "username"
        const val MEALNAME = "mealname"
        const val MEALIMAGE = "mealimage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeFragment = HomeFragment(this)
        searchFragment = SearchFragment()
        sellFragment = SellFragment()
        accountFragment = AccountFragment()

        tabLayout = findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> changeFragment(homeFragment)
                    1 -> changeFragment(searchFragment)
                    2 -> changeFragment(sellFragment)
                    3 -> changeFragment(accountFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLHomeActivity, homeFragment)
            commit()
        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLHomeActivity, fragment)
            commit()
        }
    }

    fun openMealDetailActivity(username: String, mealName: String, mealImageUrl: String){
        val intent = Intent(this@HomeActivity, MealDetailActivity::class.java)
        intent.putExtra(USERNAME, username)
        intent.putExtra(MEALNAME, mealName)
        intent.putExtra(MEALIMAGE, mealImageUrl)
        startActivity(intent)
    }
}