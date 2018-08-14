package cz.lamorak.captionthis.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.setVisible(visible: Boolean) {
    val visibility = if (visible) View.VISIBLE else View.GONE
    setVisibility(visibility)
}

fun ImageView.loadUri(uri: String, options: RequestOptions = RequestOptions()) {
    Glide.with(context)
            .load(uri)
            .apply(options)
            .into(this)
}