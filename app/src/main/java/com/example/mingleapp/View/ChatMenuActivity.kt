package com.example.mingleapp.View

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mingleapp.Adapters.UserAdapter
import com.example.mingleapp.Fragment.ProfileSettingsFragment
import com.example.mingleapp.Fragment.FavoritesFragment
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.ActivityChatMenuBinding
import com.google.android.material.navigation.NavigationView

class ChatMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityChatMenuBinding
    private lateinit var adapter: UserAdapter
    private lateinit var authVm: AuthViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel
    var favoriteFragment = FavoritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.chatMenuLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       getViewModelInstance()
        recyclerViewSetup()

        setSupportActionBar(binding.toolbar)
        binding.navMenu.setNavigationItemSelectedListener(this)
        supportActionBar?.show()

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        firebaseViewModel.users.observe(this) { usersList ->
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chat -> {
                if (this !is ChatMenuActivity) {
                    navigateToChatMenu()
                } else {
                    Toast.makeText(this, "Already in Chat Menu", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.favorites -> {
                adapter.replaceFragmentTest(this, favoriteFragment)
                supportActionBar?.title = "Favorites"
            }
            R.id.profile_settings -> {
                navigateToFragment(ProfileSettingsFragment())
                Toast.makeText(this, "profile settings selected", Toast.LENGTH_SHORT).show()
            }
            R.id.sign_out -> {
                authVm.logOut().observe(this) { isLoggedOut ->
                    if (isLoggedOut) {
                        Toast.makeText(this, "Sign out successful", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this, "Sign out failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToLogin() {
        val logOutIntent = Intent(this, LoginActivity::class.java)
        logOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logOutIntent)
        finish()
    }

    private fun navigateToChatMenu() {
        val chatIntent = Intent(this, ChatMenuActivity::class.java)
        chatIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(chatIntent)
    }

    private fun navigateToFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun recyclerViewSetup(){
        adapter = UserAdapter(mutableListOf(), this, firebaseViewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun getViewModelInstance() {
        authVm = ViewModelProvider(this)[AuthViewModel::class.java]
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
    }
}