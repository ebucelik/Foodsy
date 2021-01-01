package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.account.ReviewActivity
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.Order
import ebucelik.keepeasy.foodsy.loginOrRegister.LogInActivity
import java.lang.Exception
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var searchFragment: SearchFragment
    lateinit var sellFragment: SellFragment
    lateinit var accountFragment: AccountFragment
    lateinit var tabLayout: TabLayout
    lateinit var uuid: String

    companion object{
        const val ORDERINGUUID = "orderinguuid"
        const val OFFER = "offer"
        const val ORDER = "order"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        uuid = readUUID()

        homeFragment = HomeFragment(this)
        searchFragment = SearchFragment(this)
        sellFragment = SellFragment(uuid)
        accountFragment = AccountFragment(this, uuid)

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

    fun openOfferDetailActivity(offer: Offer){
        try {
            val intent = Intent(this@HomeActivity, OfferDetailActivity::class.java)
            intent.putExtra(ORDERINGUUID, uuid)
            intent.putExtra(OFFER, offer)
            startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun openLoginActivity(){
        val intent = Intent(this@HomeActivity, LogInActivity::class.java)
        startActivity(intent)
        finish() //The HomeActivity has to views if I click on the back button. Don't know why but to kill both I must call twice finish()
        finish()
    }

    fun openReviewActivity(order: Order){
        val intent = Intent(this@HomeActivity, ReviewActivity::class.java)
        intent.putExtra(ORDER, order)
        startActivity(intent)
    }

    private fun readUUID(): String{
        val sharedPref = this.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        return sharedPref.getString(R.string.uuid.toString(), "") as String
    }
}