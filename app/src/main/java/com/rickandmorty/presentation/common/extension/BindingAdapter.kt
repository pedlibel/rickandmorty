package com.rickandmorty.presentation.common.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("setImageFromUrl", "setPlaceholder", "setErrorPlaceholder", requireAll = false)
fun ImageView.setImageFromUrl(
    url: String?,
    placeholderRes: Drawable? = null,
    errorPlaceholderRes: Drawable? = null,
) {
    url?.let { urlNotNull ->
        Glide
            .with(this)
            .load(urlNotNull)
            .placeholder(placeholderRes)
            .error(errorPlaceholderRes)
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    } ?: run {
        Glide
            .with(this)
            .load(errorPlaceholderRes)
            .placeholder(placeholderRes)
            .error(errorPlaceholderRes)
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    }
}
