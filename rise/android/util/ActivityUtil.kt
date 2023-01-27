package com.example.app.apputil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.getStartIntent(clearTask: Boolean = false): Intent {
    return Intent(this, T::class.java).apply {
        if (clearTask) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}

inline fun <reified T : Activity> Context.startActivity(clearTask: Boolean = false) {
    val intent = getStartIntent<T>(clearTask)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.getStartIntent(clearTask: Boolean = false): Intent {
    return Intent(context, T::class.java).apply {
        if (clearTask) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}

inline fun <reified T : Activity> Fragment.startActivity(clearTask: Boolean = false) {
    val intent = getStartIntent<T>(clearTask)
    startActivity(intent)
}

fun Activity.hideSoftKeyboard() {
    val view = currentFocus
    if (view != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}