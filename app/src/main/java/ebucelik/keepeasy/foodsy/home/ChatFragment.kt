package ebucelik.keepeasy.foodsy.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.adapter.ChatAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentChatBinding
import ebucelik.keepeasy.foodsy.entitiy.ChatPool
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.repositories.ChatRepository

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)

        HomeActivity.homeActivityViewModel.chatPoolList.observe(viewLifecycleOwner, Observer{ chats ->
            binding.listViewChat.adapter = ChatAdapter((activity as HomeActivity).baseContext, chats)
        })

        ChatRepository.getChatPools(Globals.uuid)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listViewChat.setOnItemClickListener { parent, view, position, id ->
            val chatPool = parent.getItemAtPosition(position) as ChatPool
            try {
                (activity as HomeActivity).openMessageActivity(chatPool)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }
    }
}