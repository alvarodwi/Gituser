package com.varoa.gituserfav.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.databinding.ItemUserBinding

class UserRvAdapter(private val clickListener : UserRvListener)
    : ListAdapter<User, UserRvAdapter.UserVH>(
    USER_DIFF
){
    companion object{
        private val USER_DIFF = object : DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        return UserVH.inflate(
            parent
        )
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(getItem(position),clickListener)
    }

    class UserVH(private val binding : ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        //bind item user dari list ke layout item user
        fun bind(item : User, clickListener: UserRvListener){
            binding.item = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object{
            fun inflate(parent : ViewGroup) : UserVH {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return UserVH(
                    binding
                )
            }
        }
    }

    class UserRvListener(val clickListener : (item : User) -> Unit){
        fun onClick(item : User) = clickListener(item)
    }
}