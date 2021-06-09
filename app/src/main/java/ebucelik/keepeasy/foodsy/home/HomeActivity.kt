package ebucelik.keepeasy.foodsy.home
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.Globals.selectedOffer
import ebucelik.keepeasy.foodsy.Globals.selectedOrder
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.Globals.uuid
import ebucelik.keepeasy.foodsy.account.ReviewActivity
import ebucelik.keepeasy.foodsy.entitiy.ChatPool
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.Order
import ebucelik.keepeasy.foodsy.entitiy.User
import ebucelik.keepeasy.foodsy.loginOrRegister.LogInActivity
import ebucelik.keepeasy.foodsy.repositories.AccountRepository
import ebucelik.keepeasy.foodsy.viewmodels.HomeActivityViewModel
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var searchFragment: SearchFragment
    lateinit var chatFragment: ChatFragment
    lateinit var sellFragment: SellFragment
    lateinit var accountFragment: AccountFragment

    companion object{
        const val ORDERINGUUID = "orderinguuid"
        const val CHATPOOL = "chatpool"
        lateinit var homeActivityViewModel: HomeActivityViewModel
        lateinit var tabLayout: TabLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initializeUser()

        homeActivityViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        AccountRepository.getReviewQuantity(uuid)

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        chatFragment = ChatFragment()
        sellFragment = SellFragment()
        accountFragment = AccountFragment()

        tabLayout = findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> changeFragment(homeFragment)
                    1 -> changeFragment(searchFragment)
                    2 -> changeFragment(chatFragment)
                    3 -> changeFragment(sellFragment)
                    4 -> changeFragment(accountFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        changeFragment(homeFragment)
    }

    override fun onResume() {
        super.onResume()

        if(Globals.openChatTab){
            Globals.openChatTab = false
            changeFragment(chatFragment)
            val tab = tabLayout.getTabAt(2)
            tab?.select()
        }
    }

    private fun initializeUser(){
        if(user.userUUID.isEmpty()){
            val url = "${MainActivity.IP}/user?userUUID=${uuid}"

            val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback{
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()

                    Handler(Looper.getMainLooper()).post {
                        when(response.code){
                            200 -> {
                                user = gson.fromJson(body, User::class.java)
                            }
                            else -> {
                                Toast.makeText(baseContext, "Server error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.i("HomeActivity", e.toString())
                }
            })
        }
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
            //intent.putExtra(OFFER, offer)
            selectedOffer = offer;
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
        //intent.putExtra(ORDER, order)
        selectedOrder = order
        startActivity(intent)
    }

    fun openMessageActivity(chatPool: ChatPool){
        try {
            val intent = Intent(this@HomeActivity, MessageActivity::class.java)
            intent.putExtra(CHATPOOL, chatPool)
            startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}