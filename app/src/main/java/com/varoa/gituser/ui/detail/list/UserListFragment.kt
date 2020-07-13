package com.varoa.gituser.ui.detail.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varoa.gituser.databinding.UserListFragmentBinding
import com.varoa.gituser.ui.common.adapter.UserRvAdapter
import com.varoa.gituser.utils.BaseViewModel
import com.varoa.gituser.utils.showSnackbar

class UserListFragment(username : String = "",private val type : Int = 0) : Fragment() {

    companion object {
        fun newInstance(username : String,type : Int) = UserListFragment(username,type)
    }

    private lateinit var binding : UserListFragmentBinding
    private val viewModel: UserListViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(username,requireActivity().application)
        ).get(UserListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  UserListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assign viewmodel to view, also refresh list
        binding.viewModel = viewModel
        doRefresh()

        val userAdapter =
            UserRvAdapter(UserRvAdapter.UserRvListener { })
        binding.userListRv.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
        )
        binding.userListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.userListRv.adapter = userAdapter

        binding.userListSrl.setOnRefreshListener {
            doRefresh()
        }

        //observe live data
        //loading state of swipe refresh layout
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let { flag ->
                binding.userListSrl.isRefreshing = flag
            }
        })

        //error message to snackbar
        viewModel.message.observe(viewLifecycleOwner, Observer {
            binding.container.showSnackbar(it ?: "Unknown Error...")
        })

        //api result
        viewModel.getUsersData().observe(viewLifecycleOwner, Observer {
            it?.let {
                userAdapter.submitList(it)
            }
        })
    }

    private fun doRefresh(){
        viewModel.getUserSocials(type)
    }
}