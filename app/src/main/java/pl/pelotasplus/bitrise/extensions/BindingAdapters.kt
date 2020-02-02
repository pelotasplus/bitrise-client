package pl.pelotasplus.bitrise.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("requestFocus")
fun View.requestFocus(requestFocus: Boolean) {
    if (requestFocus) this.requestFocus()
}