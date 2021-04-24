package ebucelik.keepeasy.foodsy.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.Globals.uuid
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.ReceivedReviewListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentReceivedReviewBinding
import ebucelik.keepeasy.foodsy.entitiy.ReceivedReviewList
import ebucelik.keepeasy.foodsy.entitiy.Review
import okhttp3.*
import java.io.IOException

class ReceivedReviewFragment(private val uuid: String = Globals.uuid) : Fragment(R.layout.fragment_received_review) {

    private lateinit var reviewList: ReceivedReviewList
    private lateinit var binding: FragmentReceivedReviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReceivedReviewBinding.bind(view)

        this.getReviewList()
    }

    private fun getReviewList(){
        val url = "${MainActivity.IP}/reviewList?uuid=${uuid}"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                reviewList = gson.fromJson(body, ReceivedReviewList::class.java)

                activity?.runOnUiThread {
                    if(response.isSuccessful){
                        binding.reviewList.adapter = ReceivedReviewListAdapter(activity?.baseContext!!, reviewList)
                    }
                }

                response.close()
            }
        })
    }
}