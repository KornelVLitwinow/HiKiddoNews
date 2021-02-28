package com.litwinow.hikiddonews.extension

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this)
}

fun Toolbar.popScreen() {
    setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = requireActivity().currentFocus
    if (view == null) {
        view = View(requireContext())
    }

    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    val inputMethodManager =
        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = requireActivity().currentFocus
    if (view == null) {
        view = View(requireContext())
    }
    inputMethodManager.showSoftInput(view, 0)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun ImageView.loadUrlValidate(url: String, isCorrect: (Boolean) -> Unit) {
    Glide.with(context).load(url.trim()).fitCenter().listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            isCorrect.invoke(false)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            isCorrect.invoke(true)
            return false
        }

    }).into(this)
}