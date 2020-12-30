package ebucelik.keepeasy.foodsy.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OfferListAdapter
import ebucelik.keepeasy.foodsy.adapter.OrderListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentOrderBinding
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import ebucelik.keepeasy.foodsy.entitiy.OrderList
import okhttp3.*
import java.io.IOException

class OrderFragment(private val uuid:String) : Fragment(R.layout.fragment_order) {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderList: OrderList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOrderBinding.bind(view)

        getOrderedMeals()
    }

    private fun getOrderedMeals(){
        val url = "http://10.0.2.2:8080/ordering?uuid=$uuid"

        val request = Request.Builder()
                .url(url)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                orderList = gson.fromJson(body, OrderList::class.java)

                activity?.runOnUiThread {
                    binding.orderListView.adapter = OrderListAdapter(activity?.baseContext!!, orderList)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }
}