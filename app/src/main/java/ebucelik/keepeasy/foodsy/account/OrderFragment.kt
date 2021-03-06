package ebucelik.keepeasy.foodsy.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals.uuid
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OrderListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentOrderBinding
import ebucelik.keepeasy.foodsy.entitiy.Order
import ebucelik.keepeasy.foodsy.entitiy.OrderList
import ebucelik.keepeasy.foodsy.home.HomeActivity
import okhttp3.*
import java.io.IOException

class OrderFragment() : Fragment(R.layout.fragment_order) {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderList: OrderList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOrderBinding.bind(view)

        getOrderedMeals()

        binding.orderListView.setOnItemClickListener { parent, view, position, id ->
            val order = parent.getItemAtPosition(position) as Order

            try {
                (activity as HomeActivity).openReviewActivity(order)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }
    }

    private fun getOrderedMeals(){
        val url = "${MainActivity.IP}/ordering?uuid=$uuid"

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

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}