package com.litwinow.hikiddonews.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsMainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_posts)
        viewModel.insertPostsFromApiToDatabase()
    }
}