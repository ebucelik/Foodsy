package ebucelik.keepeasy.foodsy.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.Chat

class MessageAdapter(private val mMessage: List<Chat>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val personName = itemView.findViewById<TextView>(R.id.chat_name)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_message, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: Chat = mMessage.get(position)

        val txtmessage = holder.personName

        txtmessage.setText(message.message)

        val linearLayout = holder.linearLayout

        if(message.from_user == Globals.uuid)
            txtmessage.gravity = Gravity.RIGHT
        else
            linearLayout.setBackgroundColor(Color.rgb(240,240,240))
    }

    override fun getItemCount(): Int {
        return mMessage.size
    }


}