package com.varoa.gituser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.varoa.gituser.R
import com.varoa.gituser.databinding.DetailFragmentBinding
import com.varoa.gituser.ui.common.adapter.DetailVpAdapter
import com.varoa.gituser.utils.BaseViewModel
import com.varoa.gituser.utils.showSnackbar

class DetailFragment : Fragment() {
    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        viewModel = ViewModelProvider(
            this,
            BaseViewModel.Factory(args.user.id, requireActivity().application)
        ).get(DetailViewModel::class.java)

        val detailAdapter = DetailVpAdapter(
            args.user.username,
            requireContext(),
            childFragmentManager
        )
        binding.detailViewPager.adapter = detailAdapter
        binding.tabLayout.setupWithViewPager(binding.detailViewPager)

        //toolbar oh toolbar
        binding.toolbar.let {
            it.title = args.user.username
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            it.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_change_settings -> {
                        moveToSettings()
                        true
                    }
                    R.id.action_favorite -> {
                        viewModel.onFavoriteClicked(args.user,requireContext())
                        true
                    }
                    else -> false
                }
            }
        }

        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            //update favorite menu item, menu xmls can't have data binding =(
            binding.toolbar.menu.findItem(R.id.action_favorite)
                .setIcon(
                    if (user == null) R.drawable.ic_favorite_outline else R.drawable.ic_favorite
                )
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            binding.container.showSnackbar(it.toString())
        })

        return binding.root
    }

    private fun moveToSettings(){
        findNavController().navigate(
            DetailFragmentDirections.detailToSettings()
        )
    }
}