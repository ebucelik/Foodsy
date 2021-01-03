package ebucelik.keepeasy.foodsy.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OfferListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentOfferBinding
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import okhttp3.*
import java.io.IOException

class OfferFragment(private val uuid:String) : Fragment(R.layout.fragment_offer) {

    private lateinit var binding: FragmentOfferBinding
    private lateinit var offerList: OfferList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOfferBinding.bind(view)

        getOfferedMeals()
    }

    private fun getOfferedMeals(){
        val url = "http://${MainActivity.IP}:8080/offering?uuid=$uuid"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                offerList = gson.fromJson(body, OfferList::class.java)

                activity?.runOnUiThread {
                    binding.offerListView.adapter = OfferListAdapter(activity?.baseContext!!, offerList)
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}