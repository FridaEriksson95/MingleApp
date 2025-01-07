package com.example.mingleapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Model.Messages
import com.example.mingleapp.Model.Users
import com.example.mingleapp.R
import java.text.SimpleDateFormat
import java.util.Locale

const val MESSAGE_RECEIVED = 1
const val MESSAGE_SENT = 2

class MessageAdapter(
    val messagesList:MutableList<Messages>,
    val currentUserId: String,
    val onMessageDeleted : (messageId : String) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSentMessage = itemView.findViewById<TextView>(R.id.show_message_right)
        val tvSentName = itemView.findViewById<TextView>(R.id.name_tv_right)
        val tvSentTime = itemView.findViewById<TextView>(R.id.time_tv_right)
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvReceviedMessage = itemView.findViewById<TextView>(R.id.show_message_left)
        val tvReceivedName = itemView.findViewById<TextView>(R.id.name_tv_left)
        val tvReceivedTime = itemView.findViewById<TextView>(R.id.time_tv_left)
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right, parent, false)
            return SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)
            return ReceivedViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagesList[position]

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val showTimeString = timeFormat.format(message.timestamp)

        if (holder is SentViewHolder) {
            holder.tvSentMessage.text = message.text
            holder.tvSentName.text = message.senderName
            holder.tvSentTime.text = showTimeString
        } else if (holder is ReceivedViewHolder) {
            holder.tvReceviedMessage.text = message.text
            holder.tvReceivedName.text = message.senderName
            holder.tvReceivedTime.text = showTimeString
        }
    }

    fun updateData(newMessages: MutableList<Messages>) {
        messagesList.clear()
        messagesList.addAll(newMessages)
        notifyDataSetChanged()
    }
    fun deleteMessage (position: Int) {
        val messageid = messagesList[position].messageId
        messagesList.removeAt(position)
        notifyItemRemoved(position)

        if (messageid != null) {
            onMessageDeleted(messageid)
            }
        }
    }