package com.example.mingleapp.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mingleapp.R
import com.example.mingleapp.View.LoginActivity
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.FragmentProfileSettingsBinding

class ProfileSettingsFragment : Fragment() {

    private var binding: FragmentProfileSettingsBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]


        binding?.btnChangeUsername?.setOnClickListener {
            showChangeUsernameDialog()
        }

        binding?.btnDeleteAccount?.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /*
    Deletes account
     */
    private fun showDeleteAccountDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")

        builder.setPositiveButton("OK") { dialog, _ ->
            deleteAccount()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
    private fun deleteAccount() {
        authViewModel.deleteAccount(onSuccess = {
            Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_LONG).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }, onFailure = {
            Toast.makeText(requireContext(), "Account deletion failed", Toast.LENGTH_SHORT).show()
        })
    }

    /*
        Changes username
     */
    private fun showChangeUsernameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inputField = EditText(requireContext()).apply {
            hint = "Enter new username"
        }

        builder.setTitle("Change Username")
        builder.setView(inputField)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newUsername = inputField.text.toString()
            if (newUsername.isNotEmpty()) {
                val currentUserId = authViewModel.getCurrentUserId()
                firebaseViewModel.updateUsername(currentUserId, newUsername)
                Toast.makeText(requireContext(), "Username updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}