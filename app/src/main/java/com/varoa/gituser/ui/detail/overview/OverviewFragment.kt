package com.varoa.gituser.ui.detail.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varoa.gituser.databinding.OverviewFragmentBinding
import com.varoa.gituser.utils.BaseViewModel
import com.varoa.gituser.utils.showSnackbar

class OverviewFragment(username : String = "") : Fragment() {
    companion object {
        fun newInstance(username : String) =
            OverviewFragment(username)
    }

    private lateinit var binding : OverviewFragmentBinding
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(username,requireActivity().application)
        ).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewFragmentBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        doRefresh()

        binding.overviewSrl.setOnRefreshListener {
            doRefresh()
        }

        //loading state
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.overviewSrl.isRefreshing = it
                binding.overviewContent.visibility = if(it) View.GONE else View.VISIBLE
            }
        })

        //error message to snackbar
        viewModel.message.observe(viewLifecycleOwner, Observer {
            binding.overviewSrl.showSnackbar(it ?: "Unknown Error...")
        })
    }

    private fun doRefresh(){
        viewModel.getUserDetail()
    }
}