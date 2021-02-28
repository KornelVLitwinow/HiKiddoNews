package com.litwinow.hikiddonews.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.databinding.ItemSinglePostBinding
import com.litwinow.hikiddonews.extension.loadUrl
import com.litwinow.hikiddonews.model.Post

class PostViewHolder(private val binding: ItemSinglePostBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post?, listener: PostListener?) {
        with(binding) {
            if (post != null) {
                txtTitle.text = post.title
                val urlHttps = post.icon
                imgIcon.loadUrl(urlHttps)
                root.setOnClickListener { post.id?.let { it1 -> listener?.onClickPost(it1) } }
            }
        }
    }

    interface PostListener {
        fun onClickPost(id: Int)
    }

    companion object {
        fun create(parent: ViewGroup): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_single_post, parent, false)
            val binding = ItemSinglePostBinding.bind(view)
            return PostViewHolder(binding)
        }
    }
}