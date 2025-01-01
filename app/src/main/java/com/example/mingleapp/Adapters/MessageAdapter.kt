package com.example.mingleapp.Adapters

import android.content.Context
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.R

class MessageAdapter(val context: Context,
                     val messageList:MutableList<Message>,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}