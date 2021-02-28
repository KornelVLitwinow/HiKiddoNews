package com.litwinow.hikiddonews.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.litwinow.hikiddonews.model.Post
import com.litwinow.hikiddonews.viewholder.PostViewHolder

class PostsAdapter(private val listener: PostViewHolder.PostListener) :
    ListAdapter<Post, PostViewHolder>(
        POSTS_COMPARATOR
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder.create(parent)

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

private val POSTS_COMPARATOR: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem

}