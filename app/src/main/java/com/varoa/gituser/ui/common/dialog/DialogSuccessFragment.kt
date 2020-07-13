package com.varoa.gituser.ui.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.varoa.gituser.R
import com.varoa.gituser.databinding.FragmentDialogSuccessBinding


class DialogSuccessFragment : DialogFragment() {
    companion object {
        private const val DIALOG_ICON = "icon"
        private const val DIALOG_TITLE = "title"
        private const val DIALOG_SUBTITLE = "subtitle"

        fun newInstance(
            icon: Int,
            title: String,
            messsage: String
        ): DialogSuccessFragment {
            val fragment =
                DialogSuccessFragment()
            val bundle = Bundle()
            bundle.putString(DIALOG_TITLE, title)
            bundle.putString(DIALOG_SUBTITLE, messsage)
            bundle.putInt(DIALOG_ICON, icon)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDialogSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val icon = it.getInt(DIALOG_ICON)
            val title = it.getString(DIALOG_TITLE)
            val message = it.getString(DIALOG_SUBTITLE)

            Glide.with(binding.imageDialog.context)
                .load(if (icon != -1) icon else R.drawable.ic_fav)
                .into(binding.imageDialog)
            binding.textMessage.text = title
            binding.textSubMessage.text = message
        }
    }
}
