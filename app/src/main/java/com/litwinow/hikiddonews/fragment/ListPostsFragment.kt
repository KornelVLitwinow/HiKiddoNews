package com.litwinow.hikiddonews.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.adapter.PostsAdapter
import com.litwinow.hikiddonews.databinding.FragmentListPostsBinding
import com.litwinow.hikiddonews.extension.hideKeyboard
import com.litwinow.hikiddonews.viewholder.PostViewHolder
import com.litwinow.hikiddonews.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListPostsFragment : Fragment() {
    @Inject
    lateinit var viewModel: PostViewModel
    private lateinit var binding: FragmentListPostsBinding

    private val newsAdapter by lazy {
        PostsAdapter(object : PostViewHolder.PostListener {
            override fun onClickPost(id: Int) {
                findNavController().navigate(
                    R.id.action_lists_posts_fragment_to_details_fragment, bundleOf("id" to id)
                )
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        binding.rvPosts.adapter = newsAdapter
        onClickToolbar()
        setObservers()
    }

    private fun onClickToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_news -> {
                    findNavController().navigate(R.id.action_lists_posts_fragment_to_add_post_fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setObservers() {
        viewModel.observeAllPosts().observe(viewLifecycleOwner) { posts ->
            if (posts != null)
                newsAdapter.submitList(posts)
        }
    }
}