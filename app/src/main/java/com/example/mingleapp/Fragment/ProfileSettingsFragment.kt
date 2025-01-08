package com.example.mingleapp.Fragment

import android.app.AlertDialog
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
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.FragmentProfileSettingsBinding

class ProfileSettingsFragment : Fragment() {

    private var binding: FragmentProfileSettingsBinding? = null
    private lateinit var authViewModel: AuthViewModel

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




        binding?.btnDeleteAccount?.setOnClickListener {
            authViewModel.deleteAccount(onSuccess = {
                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_LONG).show()
            }, onFailure = {
                Toast.makeText(requireContext(), "Account deletion failed", Toast.LENGTH_SHORT).show()
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }











//    change profile picture = show dialog?, new picture?, new activity?
//    private fun showEditDialogChangeUserName(messageId: String, oldText: String) {
//        val builder = AlertDialog.Builder(requireContext())
//        val inputField = EditText(requireContext()).apply {
//            setText(oldText)
//        }
//
//        builder.setTitle("Edit Message")
//        builder.setView(inputField)
//
//        builder.setPositiveButton("OK") { dialog, _ ->
//            val newText = inputField.text.toString()
//            if (newText.isNotEmpty()) {
//                vm.updateMessage(messageId, chatId, newText)
//            }
//            dialog.dismiss()
//        }
//
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss()
//        }
//        builder.show()
//
//    }
//    private fun showEditDialogDeleteAccount(messageId: String, oldText: String) {
//        val builder = AlertDialog.Builder(requireContext())
//        val inputField = EditText(requireContext()).apply {
//            setText(oldText)
//        }
//
//        builder.setTitle("Edit Message")
//        builder.setView(inputField)
//
//        builder.setPositiveButton("OK") { dialog, _ ->
//            val newText = inputField.text.toString()
//            if (newText.isNotEmpty()) {
//                vm.updateMessage(messageId, chatId, newText)
//            }
//            dialog.dismiss()
//        }
//
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss()
//        }
//        builder.show()
//
//    }


}