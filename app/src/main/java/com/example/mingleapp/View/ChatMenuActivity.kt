package com.example.mingleapp.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mingleapp.Adapters.UserAdapter
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.ActivityChatMenuBinding

class ChatMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatMenuBinding
    private lateinit var adapter : UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat_menu_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        adapter = UserAdapter(mutableListOf(), this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseViewModel.users.observe(this) {usersList ->
            adapter.updateData(usersList)
        }
    }

}
