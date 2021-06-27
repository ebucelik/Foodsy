package ebucelik.keepeasy.foodsy.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.MessageAdapter
import ebucelik.keepeasy.foodsy.databinding.ActivityMessageBinding
import ebucelik.keepeasy.foodsy.entitiy.Chat
import ebucelik.keepeasy.foodsy.entitiy.ChatPool
import ebucelik.keepeasy.foodsy.repositories.ChatRepository
import ebucelik.keepeasy.foodsy.viewmodels.MessageActivityViewModel
import java.util.*

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var chatPool: ChatPool

    companion object{
        lateinit var messageActivityViewModel: MessageActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)

        messageActivityViewModel = ViewModelProvider(this).get(MessageActivityViewModel::class.java)

        binding.recyclerViewMessage.layoutManager = LinearLayoutManager(this)

        messageActivityViewModel.chatList.observe(this, { chatList ->
            binding.recyclerViewMessage.adapter = MessageAdapter(chatList)
        })

        chatPool = intent.getSerializableExtra(HomeActivity.CHATPOOL) as ChatPool

        getChat()

        writeMessage()
    }

    private fun getChat(){
        ChatRepository.getChatList(chatPool.id)
    }

    private fun writeMessage(){
        binding.sendMessage.setOnClickListener {
            val chat = Chat(0, Globals.uuid, chatPool, binding.writeMessage.text.toString(), Date())
            ChatRepository.sendMessage(chat)
            binding.writeMessage.setText("")
        }
    }
}