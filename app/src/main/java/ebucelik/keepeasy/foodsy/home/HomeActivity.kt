package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.loginOrRegister.LogInActivity
import ebucelik.keepeasy.foodsy.meal.Meal
import ebucelik.keepeasy.foodsy.meal.MealDetailActivity

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
        const val MEALCATEGORY = "mealcategory"
        const val MEALAREA = "mealarea"
        const val INGREDIENTS = "ingredients"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeFragment = HomeFragment(this)
        searchFragment = SearchFragment(this)
        sellFragment = SellFragment()
        accountFragment = AccountFragment(this)

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

        changeFragment(homeFragment)
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLHomeActivity, fragment)
            commit()
        }
    }

    fun openMealDetailActivity(username: String, mealName: String, mealImageUrl: String, mealCategory: String, mealArea: String, ingredient1: String, ingredient2: String, ingredient3: String){
        val intent = Intent(this@HomeActivity, MealDetailActivity::class.java)
        intent.putExtra(USERNAME, username)
        intent.putExtra(MEALNAME, mealName)
        intent.putExtra(MEALIMAGE, mealImageUrl)
        intent.putExtra(MEALCATEGORY, mealCategory)
        intent.putExtra(MEALAREA, mealArea)
        intent.putExtra(INGREDIENTS, "$ingredient1, $ingredient2, $ingredient3")
        startActivity(intent)
    }

    fun openLoginActivity(){
        val intent = Intent(this@HomeActivity, LogInActivity::class.java)
        startActivity(intent)
        finish() //The HomeActivity has to views if I click on the back button. Don't know why but to kill both I must call twice finish()
        finish()
    }
}