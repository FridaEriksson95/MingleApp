package com.example.mingleapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mingleapp.Adapters.UserAdapter
import com.example.mingleapp.R
import com.example.mingleapp.ViewModel.FirebaseViewModel
import com.example.mingleapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var fireBaseViewModel: FirebaseViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        fireBaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        adapter = UserAdapter(mutableListOf(), requireContext(), fireBaseViewModel)

        binding.favoritesRV.layoutManager = LinearLayoutManager(context)
        binding.favoritesRV.adapter = adapter

        fireBaseViewModel.favoriteUsers.observe(viewLifecycleOwner) { favoriteUsers ->
            adapter.updateData(favoriteUsers)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}