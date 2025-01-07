package com.example.mingleapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Adapters.MessageAdapter
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.AuthViewModel
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.ViewModel.MessageViewModel
import com.example.mingleapp.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    lateinit var messageAdapter: MessageAdapter
    lateinit var vm: MessageViewModel
    private lateinit var chatId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(MessageViewModel::class.java)

        val currentUserId = vm.getCurrentUserId()
        val otherUser = arguments?.getString("uid") ?: ""
        val otherUserName = arguments?.getString("userName") ?: "Error"

        binding.nameChatTv.text = otherUserName

        chatId = if (currentUserId > otherUser) {
            "$currentUserId-$otherUser"
        } else {
            "$otherUser-$currentUserId"
        }

        setupRecycleView()
        vm.listenForMessages(chatId)

        vm.users.observe(viewLifecycleOwner) { userList ->
            vm.messages.observe(viewLifecycleOwner) { messageList ->

                val updateMessages = messageList.map { message ->
                    val sender = userList.find { it.uid == message.senderId }
                    message.senderName = sender?.userName ?: "unknown"
                    message
                }
                messageAdapter.updateData(updateMessages.toMutableList())
                binding.chatRv.scrollToPosition(updateMessages.size - 1)
            }
        }

        binding.sendBtn.setOnClickListener {

            val inputMessage = binding.typeMessageEt.text.toString()
            if (inputMessage.isNotEmpty()) {

                vm.sendMessage(inputMessage, chatId)
                binding.typeMessageEt.text.clear()

            } else {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.likeBtn.setOnClickListener {

            val thumbsUp = "\uD83D\uDC4D"
            vm.sendMessage(thumbsUp, chatId)

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().findViewById<View>(R.id.chat_menu_layout).visibility = View.VISIBLE
            requireActivity().findViewById<View>(R.id.fragment_container).visibility = View.GONE
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Allows vertical movement
            ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                messageAdapter.deleteMessage(position)

            }

        })
          itemTouchHelper.attachToRecyclerView(binding.chatRv)

    }

    private fun setupRecycleView() {
        messageAdapter =
            MessageAdapter(mutableListOf(), vm.getCurrentUserId()) { messageId ->
                vm.deleteMessage(messageId, chatId)
            }
        binding.chatRv.adapter = messageAdapter
        binding.chatRv.layoutManager = LinearLayoutManager(requireContext())
    }
}
