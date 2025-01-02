package com.example.mingleapp.View

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.databinding.ActivityLoginBinding
import com.google.firebase.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var authVm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authVm = ViewModelProvider(this).get(AuthViewModel::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupTv.setOnClickListener {
            navigateToSignUp()
        }

        binding.loginBtn.setOnClickListener {
            login()
        }

        binding.forgotPw.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if(dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
    }

    private fun login() {

        val email = binding.loginEmail.text.toString()
        val password = binding.loginPw.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        authVm.signIn(email, password, onSuccess = {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            navigateToChatMenu()
        }, onFailure = {

            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()

        })

    }
    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToChatMenu() {
        val intent = Intent(this, ChatMenuActivity::class.java)
        startActivity(intent)
    }

    private fun compareEmail(email: EditText) {
        val email = email.text.toString().trim()

        if (email.isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return
        }
        authVm.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}