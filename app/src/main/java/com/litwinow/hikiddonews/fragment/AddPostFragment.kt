package com.litwinow.hikiddonews.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.databinding.FragmentAddPostBinding
import com.litwinow.hikiddonews.extension.hideKeyboard
import com.litwinow.hikiddonews.extension.loadUrlValidate
import com.litwinow.hikiddonews.extension.popScreen
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
class AddPostFragment : Fragment() {
    @Inject
    lateinit var viewModel: PostViewModel
    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbar()
        setObservers()
        imagePostCorrectListener()
    }

    private fun imagePostCorrectListener() {
        binding.editTextIcon.addTextChangedListener {
            binding.imagePreview.loadUrlValidate(it.toString()) { isCorrect ->
                viewModel.isCorrectImageUrl.value = isCorrect
            }
        }
    }

    private fun setObservers() {
        viewModel.observeIdAddedPost.observe(viewLifecycleOwner) { id ->
            if (id != null) {
                hideKeyboard()
                binding.root.clearFocus()
                findNavController().navigate(
                    R.id.action_add_post_fragment_to_details_fragment, bundleOf(
                        "id" to id
                    )
                )
            }
        }

        viewModel.observeValidationResult.observe(viewLifecycleOwner) { map ->
            if (map != null) {
                for (key in map.keys) {
                    invalidateInputs(key, map.getValue(key))
                }
            }
        }

        viewModel.isCorrectImageUrl.observe(viewLifecycleOwner) { isCorrect ->
            if (!isCorrect) {
                binding.inputLayoutIcon.error = getString(R.string.error_image_url)
            } else
                binding.inputLayoutIcon.error = null
        }
    }

    private fun invalidateInputs(
        key: InputTypeValidation,
        validationState: ValidationState
    ) {
        when (key) {
            HEADER -> handleInputError(binding.inputLayoutHeader, validationState)
            DESCRIPTION -> handleInputError(
                binding.inputLayoutDescription,
                validationState
            )
        }
    }

    private fun handleInputError(
        inputLayout: TextInputLayout?,
        validationState: ValidationState
    ) {
        when (validationState) {
            Valid -> inputLayout?.error = null
            is Invalid -> inputLayout?.error =
                getString(validationState.errorMessage)
        }
    }

    private fun onClickToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.save_post) {
                addNewPost()
                true

            } else false
        }
        binding.toolbar.popScreen()
    }

    private fun addNewPost() {
        with(binding) {
            if (editTextIcon.text.isNullOrEmpty()) {
                inputLayoutIcon.error = getString(R.string.error_empty_field)
            }
            val header = editTextHeader.text.toString().trim()
            val description = editTextDescription.text.toString().trim()
            val icon = editTextIcon.text.toString().trim()
            viewModel.submitAddPost(Post(null, description, icon, header))
        }
    }
}