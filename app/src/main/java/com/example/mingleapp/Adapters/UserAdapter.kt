package com.example.mingleapp.Adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Fragment.ChatFragment
import com.example.mingleapp.Fragment.FavoritesFragment
import com.example.mingleapp.Model.Users
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.FirebaseViewModel

class UserAdapter(
    private val users: MutableList<Users>,
    private val context: Context,
    private val firebaseViewModel: FirebaseViewModel
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        val profilePic : ImageView = itemView.findViewById(R.id.recyclerIv)
        val favoriteBtn : Button = itemView.findViewById(R.id.favorite_btn)
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
        holder.nameTextView.text = user.userName
        holder.profilePic.setImageResource(R.drawable.baseline_person_24)

        val favoriteIcon = if(user.isFavorite) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24
        holder.favoriteBtn.setBackgroundResource(favoriteIcon)

        holder.favoriteBtn.setOnClickListener {
            user.isFavorite = !user.isFavorite
            firebaseViewModel.updateFavoriteStatus(user)
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            if (user.isFavorite) {
                replaceFragment(FavoritesFragment())
            } else {
                val chatFragment = ChatFragment()
                val bundle = Bundle().apply {
                    putString("uid", user.uid)
                    putString("userName", user.userName)
                }
                replaceFragment(chatFragment, bundle)
            }
        }
    }

    fun replaceFragmentTest(context : Context, fragment : Fragment) {
        val activity = context as AppCompatActivity

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        activity.findViewById<View>(R.id.chat_menu_layout).visibility = View.GONE
        activity.findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle? = null) {
        val activity = context as AppCompatActivity

        bundle?.let { fragment.arguments = it }

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        activity.findViewById<View>(R.id.chat_menu_layout).visibility = View.GONE
        activity.findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
    }

    fun updateData (newUsers : MutableList<Users>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}