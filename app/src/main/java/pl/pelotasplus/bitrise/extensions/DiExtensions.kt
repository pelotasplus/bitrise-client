package pl.pelotasplus.bitrise.extensions

import androidx.fragment.app.Fragment
import pl.pelotasplus.bitrise.BitriseApp

val Fragment.appComponent
    get() = (requireContext().applicationContext as BitriseApp).appComponent