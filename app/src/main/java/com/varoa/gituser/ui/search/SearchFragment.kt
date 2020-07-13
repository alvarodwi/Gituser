package com.varoa.gituser.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varoa.gituser.R
import com.varoa.gituser.data.model.User
import com.varoa.gituser.databinding.SearchFragmentBinding
import com.varoa.gituser.ui.common.adapter.UserRvAdapter
import com.varoa.gituser.utils.showSnackbar
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {
    companion object {
        private const val TRIGGER_SEARCH_FIELD = 100
        private val SEARCH_FIELD_DELAY = TimeUnit.SECONDS.toMillis(1)
    }

    private lateinit var binding: SearchFragmentBinding
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //assign viewmodel WHEN fragment's view created
        binding.viewModel = viewModel

        //toolbar
        binding.toolbar.let {
            it.title = getString(R.string.app_name)
            it.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_change_settings -> {
                        moveToSettings()
                        true
                    }
                    R.id.action_favorite -> {
                        moveToFavorite()
                        true
                    }
                    else -> false
                }
            }
        }

        //search field
        binding.searchField.editText?.doOnTextChanged { _, _, _, _ ->
            searchFieldHandler.removeMessages(TRIGGER_SEARCH_FIELD)
            searchFieldHandler.sendEmptyMessageDelayed(TRIGGER_SEARCH_FIELD, SEARCH_FIELD_DELAY)
        }

        //rv
        val searchAdapter =
            UserRvAdapter(UserRvAdapter.UserRvListener { data ->
                Timber.d("Search :: On Click -> ${data.username}")
                moveToDetail(data)
            })
        binding.searchRv.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
        )
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRv.adapter = searchAdapter

        binding.searchSrl.isEnabled = false

        //observe from viewmodel
        //loading state of swipe refresh layout
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let { flag ->
                binding.searchSrl.isRefreshing = flag
            }
        })

        //error message to snackbar
        viewModel.message.observe(viewLifecycleOwner, Observer {
            binding.container.showSnackbar(it ?: "Unknown Error...")
        })

        //api result
        viewModel.getSearchResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("Search :: Result -> $it")
                searchAdapter.submitList(it)
            }
        })
    }

    private val searchFieldHandler = Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == TRIGGER_SEARCH_FIELD) {
            if (!TextUtils.isEmpty(binding.searchField.editText?.text)) {
                viewModel.commenceSearch(binding.searchField.editText?.text.toString())
            }
        }
        false
    }

    private fun moveToDetail(user: User) {
        findNavController().navigate(
            SearchFragmentDirections.searchToDetail(user)
        )
    }

    private fun moveToFavorite() {
        findNavController().navigate(
            SearchFragmentDirections.searchToFavorite()
        )
    }

    private fun moveToSettings(){
        findNavController().navigate(
            SearchFragmentDirections.searchToSettings()
        )
    }
}