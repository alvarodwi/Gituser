package com.varoa.gituser.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varoa.gituser.R
import com.varoa.gituser.data.model.User
import com.varoa.gituser.databinding.FavoriteFragmentBinding
import com.varoa.gituser.ui.common.adapter.UserRvAdapter
import timber.log.Timber

class FavoriteFragment : Fragment() {
    private lateinit var binding: FavoriteFragmentBinding
    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.favoriteSrl.isRefreshing = false

        val adapter =
            UserRvAdapter(UserRvAdapter.UserRvListener { data ->
                Timber.d("Search :: On Click -> ${data.username}")
                moveToDetail(data)
            })

        binding.toolbar.let {
            it.title = getString(R.string.favorite_user)
            it.menu.findItem(R.id.action_favorite).let { menuFav ->
                menuFav.isVisible = false
                menuFav.isEnabled = false
            }
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            it.setOnMenuItemClickListener {menuItem ->
                when(menuItem.itemId){
                    R.id.action_change_settings -> {
                        moveToSettings()
                        true
                    }
                    else -> false
                }
            }
        }

        binding.favoriteRv.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
        )
        binding.favoriteRv.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRv.adapter = adapter

        //disable swipe refresh, because the data is automatically updated...
        binding.favoriteSrl.isEnabled = false

        viewModel.userList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                binding.favoriteSrl.isRefreshing = false
                adapter.submitList(it)
            }
        })
    }

    private fun moveToDetail(user: User) {
        findNavController().navigate(
            FavoriteFragmentDirections.favoriteToDetail(user)
        )
    }

    private fun moveToSettings(){
        findNavController().navigate(
            FavoriteFragmentDirections.favoriteToSettings()
        )
    }

}