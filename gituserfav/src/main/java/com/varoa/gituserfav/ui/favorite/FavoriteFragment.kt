package com.varoa.gituserfav.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varoa.gituserfav.R
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.databinding.FavoriteFragmentBinding
import com.varoa.gituserfav.ui.adapter.UserRvAdapter
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
            it.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_change_settings -> {
                        Intent(Settings.ACTION_LOCALE_SETTINGS).also{ intent ->
                            startActivity(intent)
                        }
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

        binding.favoriteSrl.setOnRefreshListener {
            viewModel.refreshUserList()
        }

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
}