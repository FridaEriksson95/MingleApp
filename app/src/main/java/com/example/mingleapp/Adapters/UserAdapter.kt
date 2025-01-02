package com.example.mingleapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Fragment.ChatFragment
import com.example.mingleapp.Model.Users
import com.example.mingleapp.R

class UserAdapter(
    private val users: MutableList<Users>,
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        val profilePic : ImageView = itemView.findViewById(R.id.recyclerIv)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_menu_user,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.nameTextView.text = users[position].userName
        holder.profilePic.setImageResource(users[position].imageResourceID)

        holder.itemView.setOnClickListener {

            val chatFragment = ChatFragment()

            val activity = context as AppCompatActivity

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()

            activity.findViewById<View>(R.id.chat_menu_layout).visibility = View.GONE
            activity.findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE

        }

    }

    fun updateData (newUsers : MutableList<Users>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}