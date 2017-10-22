package de.beuth.test

/**
 * Created by Benjamin Rühl on 22.10.2017.
 */
import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) as T }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) as T }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)