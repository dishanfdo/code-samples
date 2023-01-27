@file:Suppress("unused")

package com.thehollisco.hollis.app.apputil

import android.app.Activity
import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import com.thehollisco.hollis.common_util.Validation
import com.thehollisco.hollis.common_util.ValidationError

fun EditText.updateFieldWithValidation(
    validation: Validation, handler: EditText.(List<ValidationError>) -> Unit
) {
    when (validation) {
        is Validation.Valid -> error = null
        is Validation.Error -> handler(validation.errors)
    }
}

open class TextWatcherAdapter : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

fun EditText.onTextChanged(block: () -> Unit) {
    this.addTextChangedListener(object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block()
        }
    })
}

fun EditText.onTextChange(
    after: ((Editable) -> Unit)? = null,
    before: ((s: CharSequence, start: Int, count: Int, after: Int) -> Unit)? = null,
    onChange: ((s: CharSequence, start: Int, before: Int, count: Int) -> Unit)? = null
): TextWatcher {

    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (after != null) {
                after(s)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (before != null) {
                before(s, start, count, after)
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (onChange != null) {
                onChange(s, start, before, count)
            }
        }

    }

    addTextChangedListener(watcher)
    return watcher
}

fun View.center(): Point {
    val x = (left + right) / 2
    val y = (top + bottom) / 2
    return Point(x, y)
}

fun View.centerInWindow(): Point {
    val originInWindow = IntArray(2)
    getLocationInWindow(originInWindow)
    val centerX = originInWindow[0] + width / 2
    val centerY = originInWindow[1] + height / 2
    return Point(centerX, centerY)
}

fun View.windowLocationToInsideLocation(windowLocation: Point): Point {
    val x = windowLocation.x - left
    val y = windowLocation.y - top
    return Point(x, y)
}

fun View.window(): Window? {
    return (context as? Activity)?.window
}