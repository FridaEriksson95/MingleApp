package com.example.mingleapp.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Model.Messages
import com.example.mingleapp.R

const val MESSAGE_RECEIVED = 1
const val MESSAGE_SENT = 2

class MessageAdapter(val context: Context,
                     val messagesList:MutableList<Messages>,
                     val currentUserId: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        Chat right -> sends message
        val tvMessage = itemView.findViewById<TextView>(R.id.show_message_right)
        val tvName = itemView.findViewById<TextView>(R.id.name_tv_right)

    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        Chat left -> receives message
        val tvMessage = itemView.findViewById<TextView>(R.id.show_message_left)
        val tvName = itemView.findViewById<TextView>(R.id.name_tv_left)

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messagesList[position]

        return if (currentMessage.senderId == currentUserId) {
            MESSAGE_SENT
        } else {
            MESSAGE_RECEIVED
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
       return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}