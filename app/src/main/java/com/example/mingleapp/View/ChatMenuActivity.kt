package com.example.mingleapp.View

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mingleapp.Adapters.UserAdapter
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.ActivityChatMenuBinding
import com.google.firebase.auth.FirebaseAuth

class ChatMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatMenuBinding
    private lateinit var adapter : UserAdapter
    private lateinit var authVm : AuthViewModel

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
         authVm = ViewModelProvider(this)[AuthViewModel::class.java]

        adapter = UserAdapter(mutableListOf(), this)
        setSupportActionBar(binding.toolbar)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        firebaseViewModel.users.observe(this) {usersList ->
            adapter.updateData(usersList)
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    firebaseViewModel.onQueryTextChange(newText)
                }
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.sign_out -> {
                    authVm.logOut().observe(this) { isLoggedOut ->
                        if (isLoggedOut) {
                            Toast.makeText(this, "Sign out successful..", Toast.LENGTH_SHORT).show()
                            navigateToLogin()
                        } else {
                            Toast.makeText(this, "Sign out failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                } else ->super.onOptionsItemSelected(item)
            }
    }
    private fun navigateToLogin() {
        val logOutIntent = Intent(this, LoginActivity::class.java)
        logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logOutIntent)
        finish()
    }
}
