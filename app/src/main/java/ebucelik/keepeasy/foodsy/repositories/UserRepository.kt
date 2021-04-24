package ebucelik.keepeasy.foodsy.repositories

import android.os.Handler
import android.os.Looper
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.account.UserActivity
import ebucelik.keepeasy.foodsy.home.HomeActivity
import okhttp3.*
import java.io.IOException

object UserRepository {

    fun getReviewQuantity(uuid: String) {
        val url = "${MainActivity.IP}/reviewQuantity?uuid=${uuid}"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                Handler(Looper.getMainLooper()).post {
                    if(response.code == 200){
                        if (body != null) {
                            UserActivity.userActivityViewModel.setReviewQuantity(body.toInt())
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}