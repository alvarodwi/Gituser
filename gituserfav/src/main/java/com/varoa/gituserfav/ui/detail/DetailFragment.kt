package com.varoa.gituserfav.ui.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varoa.gituserfav.R
import com.varoa.gituserfav.databinding.DetailFragmentBinding
import com.varoa.gituserfav.utils.BaseViewModel
import timber.log.Timber

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
        binding.viewModel = viewModel

        //toolbar oh toolbar
        binding.toolbar.let {
            it.title = args.user.username
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            Timber.d("Detail :: User Data : $it")
        })
    }
}