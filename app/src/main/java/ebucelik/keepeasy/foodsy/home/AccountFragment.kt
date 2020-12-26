package ebucelik.keepeasy.foodsy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.account.OfferFragment
import ebucelik.keepeasy.foodsy.account.OrderFragment
import ebucelik.keepeasy.foodsy.databinding.FragmentAccountBinding
import java.util.ArrayList
import kotlin.math.round

class AccountFragment(home: HomeActivity) : Fragment(R.layout.fragment_account) {

    private val review = 3.4
    private lateinit var offerFragment: OfferFragment
    private lateinit var orderFragment: OrderFragment
    private val homeActivity: HomeActivity = home
    private lateinit var rates: ArrayList<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Binding don't work. Don't know why exactly but with the included TabItems it don't work.
        val accountTabLayout = view.findViewById<TabLayout>(R.id.accountTabLayout)

        rates = arrayListOf(
                view.findViewById(R.id.rate1),
                view.findViewById(R.id.rate2),
                view.findViewById(R.id.rate3),
                view.findViewById(R.id.rate4),
                view.findViewById(R.id.rate5)
        )

        offerFragment = OfferFragment()
        orderFragment = OrderFragment()

        setStarReviews()

        accountTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> changeFragment(offerFragment)
                    1 -> changeFragment(orderFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        changeFragment(offerFragment)
    }

    private fun changeFragment(fragment: Fragment){
        homeActivity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLAccountFragment, fragment)
            commit()
        }
    }

    private fun setStarReviews(){
        val reviewInt = round(review).toInt()

        if(reviewInt >= 1){
            for (i in 0 until reviewInt){
                rates[i].setImageResource(R.drawable.ic_round_star_rate_24_gold)
            }
        }
    }
}