package ebucelik.keepeasy.foodsy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.entitiy.User
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.loginOrRegister.LogInActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object{
        //const val IP = "http://10.0.2.2:8080"
        const val IP = "https://myfood-backend.herokuapp.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUser(readUUID())
    }

    private fun showLogInScreen() {
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, LogInActivity::class.java))
            finish() //If entity go back, the app closes.
        }, 3000)
    }

    private fun showHomeScreen() {
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish() //If entity go back, the app closes.
        }, 3000)
    }

    private fun readUUID(): String{
        val sharedPref = this.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        return sharedPref.getString(R.string.uuid.toString(), "") as String
    }

    private fun getUser(uuid: String){
        val url = "${IP}/user?userUUID=${uuid}"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){

                val body = response.body?.string()
                val gson = GsonBuilder().create()

                Handler(Looper.getMainLooper()).post {
                    when(response.code){
                        200 -> {
                            user = gson.fromJson(body, User::class.java)
                            Globals.uuid = user.userUUID

                            showHomeScreen()
                        }
                        else -> {
                            showLogInScreen()
                        }
                    }
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}