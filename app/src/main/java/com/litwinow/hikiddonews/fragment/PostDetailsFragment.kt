package com.litwinow.hikiddonews.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.databinding.FragmentDetailsPostBinding
import com.litwinow.hikiddonews.extension.*
import com.litwinow.hikiddonews.model.Post
import com.litwinow.hikiddonews.validator.PostValidator.InputTypeValidation
import com.litwinow.hikiddonews.validator.PostValidator.InputTypeValidation.DESCRIPTION
import com.litwinow.hikiddonews.validator.PostValidator.InputTypeValidation.HEADER
import com.litwinow.hikiddonews.validator.PostValidator.ValidationState
import com.litwinow.hikiddonews.validator.PostValidator.ValidationState.Invalid
import com.litwinow.hikiddonews.validator.PostValidator.ValidationState.Valid
import com.litwinow.hikiddonews.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: PostViewModel
    private lateinit var binding: FragmentDetailsPostBinding

    private var post: Post? = null
    private var idPost: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPost = arguments?.getInt("id") as Int
        setObservers()
        onClickToolbar()
        imagePostCorrectListener()
    }

    private fun setObservers() {
        viewModel.getPostByIdFromDatabase(idPost).observe(viewLifecycleOwner) { viewModelPost ->
            post = viewModelPost
            initView(viewModelPost)
        }

        viewModel.observeValidationResult.observe(viewLifecycleOwner) { map ->
            if (map != null) {
                for (key in map.keys) {
                    invalidateInputs(key, map.getValue(key))
                }
            }
        }

        viewModel.observeSavedPost.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess != null && isSuccess == true) {
                disableEditPost()
            }
        }

        viewModel.isCorrectImageUrl.observe(viewLifecycleOwner) { isCorrect ->
            if (!isCorrect) {
                binding.inputLayoutImgUrl.error = getString(R.string.error_image_url)
            } else
                binding.inputLayoutImgUrl.error = null
        }
    }

    private fun imagePostCorrectListener() {
        binding.txtImgUrl.addTextChangedListener {
            binding.imgIcon.loadUrlValidate(it.toString()) { isCorrect ->
                viewModel.isCorrectImageUrl.value = isCorrect
            }
        }
    }

    private fun disableEditPost() {
        with(binding) {
            toolbar.menu?.removeItem(R.id.save_post)
            toolbar.inflateMenu(R.menu.edit_menu)
            txtHeader.isFocusableInTouchMode = false
            txtDescription.isFocusableInTouchMode = false
            hideKeyboard()
            inputLayoutImgUrl.gone()
            root.clearFocus()
        }
    }

    private fun invalidateInputs(
        inputTypeValidation: InputTypeValidation,
        validationState: ValidationState
    ) {
        when (inputTypeValidation) {
            HEADER -> handleInputError(binding.inputLayoutHeader, validationState)
            DESCRIPTION -> handleInputError(
                binding.inputLayoutDescription, validationState
            )
        }
    }

    private fun handleInputError(
        inputLayout: TextInputLayout?,
        validationState: ValidationState
    ) {
        when (validationState) {
            Valid -> inputLayout?.error = null
            is Invalid -> inputLayout?.error = getString(validationState.errorMessage)
        }
    }

    private fun initView(post: Post) {
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            txtHeader.setText(post.title)
            txtCounter.text =
                getString(R.string.counter_characters, post.description.length.toString())
            txtDescription.setText(post.description)
            txtImgUrl.setText(post.icon)
        }
    }

    private fun onClickToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_post -> {
                    onClickEditIcon()
                    true
                }
                R.id.save_post -> {
                    savePostToDatabase()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.popScreen()
    }

    private fun savePostToDatabase() {
        with(binding) {
            val description = txtDescription.text.toString().trim()
            val icon = txtImgUrl.text.toString().trim()
            val title = txtHeader.text.toString().trim()
            viewModel.submitUpdatedPost(Post(idPost, description, icon, title))
        }
    }

    private fun onClickEditIcon() {
        with(binding) {
            txtHeader.isFocusableInTouchMode = true
            txtDescription.isFocusableInTouchMode = true
            txtHeader.requestFocus()
            txtHeader.setSelection(txtHeader.text?.length ?: 0)
            toolbar.menu?.removeItem(R.id.edit_post)
            toolbar.inflateMenu(R.menu.save_menu)
            inputLayoutImgUrl.show()
            showKeyboard()
        }
    }
}