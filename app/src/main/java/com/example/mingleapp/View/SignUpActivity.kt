package com.example.mingleapp.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var authVm: AuthViewModel
    lateinit var profileImagePicker : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authVm = ViewModelProvider(this).get(AuthViewModel::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profileImagePicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val selectedImage = result.data?.data
                    binding.profileImage.setImageURI(selectedImage)
                }
            }
        binding.profileImage.setOnClickListener {
            val profilePickIntent = Intent(Intent.ACTION_PICK)
            profilePickIntent.type = "image/*"
            profileImagePicker.launch(profilePickIntent)
        }

        binding.loginBtn.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val email = binding.signupEmail.text.toString()
        val password = binding.signupPw.text.toString()
        val confirmPassword = binding.signupConfirm.text.toString()
        val username = binding.signupUsername.text.toString()
        val birth = binding.signupBirthdate.text.toString()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || birth.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return
        }
        binding.progressBar.visibility = View.VISIBLE

        authVm.createAccount(birth, username, email, password, imageResourceID = 0, onSuccess = {
            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show()
            navigateToLogin()
        }, onFailure = {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
        },
        )
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}