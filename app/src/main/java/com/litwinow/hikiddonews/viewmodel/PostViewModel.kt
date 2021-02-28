package com.litwinow.hikiddonews.viewmodel

import androidx.lifecycle.*
import com.litwinow.hikiddonews.model.Post
import com.litwinow.hikiddonews.repository.PostRepository
import com.litwinow.hikiddonews.validator.PostValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val postValidator: PostValidator
) : ViewModel() {

    private val idAddedPostMutable = MutableLiveData<Int>()
    val observeIdAddedPost: LiveData<Int> = idAddedPostMutable

    private val validationResultMutable =
        MutableLiveData<Map<PostValidator.InputTypeValidation, PostValidator.ValidationState>>()
    val observeValidationResult: LiveData<Map<PostValidator.InputTypeValidation, PostValidator.ValidationState>> =
        validationResultMutable

    private val successUpdatePostMutable = MutableLiveData<Boolean>()
    val observeSavedPost: LiveData<Boolean> = successUpdatePostMutable

    var isCorrectImageUrl = MutableLiveData(true)

    fun observeAllPosts() = postRepository.getAllPostsDatabase().asLiveData()

    fun getPostByIdFromDatabase(id: Int) = postRepository.getPostDatabaseById(id).asLiveData()

    fun insertPostsFromApiToDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = postRepository.getListPosts()
                if (response.isSuccessful) {
                    postRepository.insertAllPosts(response.body()?.posts)
                } else {
                    Timber.e("Something goes wrong ${response.message()}")
                }
            } catch (e: Exception) {
                Timber.e("Something goes wrong ${e.message}")
            }
        }
    }

    private fun addPostToDatabase(post: Post) {
        viewModelScope.launch {
            try {
                val idAddedNews = postRepository.addPost(post)
                idAddedPostMutable.postValue(idAddedNews.toInt())
            } catch (e: Exception) {
                Timber.e("Something goes wrong ${e.message}")
            }
        }
    }

    fun submitAddPost(post: Post) {
        val validationMap = postValidator.validateFields(post.title, post.description)
        validationResultMutable.value = validationMap
        if (postValidator.isValid(validationMap) && isCorrectImageUrl.value == true) {
            addPostToDatabase(post)
        }
    }

    fun submitUpdatedPost(post: Post) {
        val validationMap = postValidator.validateFields(post.title, post.description)
        validationResultMutable.value = validationMap
        if (postValidator.isValid(validationMap) && isCorrectImageUrl.value == true) {
            updatePostDatabase(post)
        }
    }

    private fun updatePostDatabase(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.updatePost(post)
                successUpdatePostMutable.postValue(true)
            } catch (e: Exception) {
                Timber.e("Something goes wrong ${e.message}")
            }
        }
    }

}