package com.example.mingleapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Model.Messages
import com.example.mingleapp.Model.Users
import com.example.mingleapp.R

const val MESSAGE_RECEIVED = 1
const val MESSAGE_SENT = 2

class MessageAdapter(val context: Context,
                     val messagesList:MutableList<Messages>,
                     val currentUserId: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSentMessage = itemView.findViewById<TextView>(R.id.show_message_right)
        val tvSentName = itemView.findViewById<TextView>(R.id.name_tv_right)
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvReceviedMessage = itemView.findViewById<TextView>(R.id.show_message_left)
        val tvReceivedName = itemView.findViewById<TextView>(R.id.name_tv_left)
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
        if (viewType == MESSAGE_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false)
            return SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false)
            return ReceivedViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
       return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagesList[position]

        if (holder is SentViewHolder) {
            holder.tvSentMessage.text = message.text
            holder.tvSentName.text = message.senderName
        } else if (holder is ReceivedViewHolder) {
            holder.tvReceviedMessage.text = message.text
            holder.tvReceivedName.text = message.senderName
        }
    }

    fun updateData(newMessages: MutableList<Messages>) {
        messagesList.clear()
        messagesList.addAll(newMessages)
        notifyDataSetChanged()
    }
}