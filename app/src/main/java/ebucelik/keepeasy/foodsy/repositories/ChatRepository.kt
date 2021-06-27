package ebucelik.keepeasy.foodsy.repositories

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.entitiy.*
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.home.MessageActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime

object ChatRepository {
    fun getChatPools(uuid: String) {
        val url = "${MainActivity.IP}/chatpool?uuid=${uuid}"
        var chatPoolList: ChatPoolList

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
                            val gson = GsonBuilder().create()
                            chatPoolList = gson.fromJson(body, ChatPoolList::class.java)

                            HomeActivity.homeActivityViewModel.chatPoolList.value = chatPoolList.chatPoolList
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatList(id: Long) {
        val url = "${MainActivity.IP}/chatList?id=${id}"
        var chatList: ChatList

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
                            val gson = GsonBuilder().create()
                            chatList = gson.fromJson(body, ChatList::class.java)

                            MessageActivity.messageActivityViewModel.chatList.value = chatList.chatList
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    fun createChatPool(chatPool: ChatPool, context: Context){
        val url = "${MainActivity.IP}/chatpool"

        val jsonObject = JSONObject()
        val jsonObjectTest = JSONObject()
        val jsonObjectTest2 = JSONObject()

        try {
            jsonObjectTest
                .put("id", chatPool.user.id)
                .put("userUUID", chatPool.user.userUUID)
                .put("username", chatPool.user.username)
                .put("firstname", chatPool.user.firstname)
                .put("surname", chatPool.user.surname)
                .put("profileImage", chatPool.user.profileImage)
            jsonObjectTest2
                .put("id", chatPool.withUser.id)
                .put("userUUID", chatPool.withUser.userUUID)
                .put("username", chatPool.withUser.username)
                .put("firstname", chatPool.withUser.firstname)
                .put("surname", chatPool.withUser.surname)
                .put("profileImage", chatPool.withUser.profileImage)
            jsonObject
                .put("user", jsonObjectTest)
                .put("withUser", jsonObjectTest2)
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)


        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Handler(Looper.getMainLooper()).post {
                    if(response.code == 201){
                        Toast.makeText(context, "Meal is successfully ordered.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }

    fun sendMessage(chat: Chat){
        val url = "${MainActivity.IP}/chat"

        val jsonObject = JSONObject()
        val jsonObject1 = JSONObject()
        val jsonObject2 = JSONObject()
        val jsonObject3 = JSONObject()

        try {
            jsonObject3
                .put("id", chat.chatPool.user.id)
                .put("userUUID", chat.chatPool.user.userUUID)
                .put("username", chat.chatPool.user.username)
                .put("firstname", chat.chatPool.user.firstname)
                .put("surname", chat.chatPool.user.surname)
                .put("profileImage", chat.chatPool.user.profileImage)
            jsonObject2
                .put("id", chat.chatPool.withUser.id)
                .put("userUUID", chat.chatPool.withUser.userUUID)
                .put("username", chat.chatPool.withUser.username)
                .put("firstname", chat.chatPool.withUser.firstname)
                .put("surname", chat.chatPool.withUser.surname)
                .put("profileImage", chat.chatPool.withUser.profileImage)
            jsonObject1
                .put("id", chat.chatPool.id)
                .put("user", jsonObject2)
                .put("withUser", jsonObject3)
            jsonObject
                .put("from_user", chat.from_user)
                .put("chatPool", jsonObject1)
                .put("message", chat.message)
                .put("timestamp", LocalDateTime.now())
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                Handler(Looper.getMainLooper()).post {
                    if(response.code == 200){
                        val gson = GsonBuilder().create()
                        val chat = gson.fromJson(body, Chat::class.java)

                        getChatList(chat.chatPool.id)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}