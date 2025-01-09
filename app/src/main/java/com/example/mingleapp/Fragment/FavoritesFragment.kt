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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireBaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        recyclerViewSetup()

        fireBaseViewModel.favoriteUsers.observe(viewLifecycleOwner) { favoriteUsers ->
            adapter.updateData(favoriteUsers)
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    private fun recyclerViewSetup(){
        adapter = UserAdapter(mutableListOf(), requireContext(), fireBaseViewModel)
        binding.favoritesRV.layoutManager = LinearLayoutManager(context)
        binding.favoritesRV.adapter = adapter
    }
}