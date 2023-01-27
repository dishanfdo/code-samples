package com.example.app.apputil

import android.graphics.Bitmap

fun resizeBitmap(
    src: Bitmap,
    targetWidth: Int? = null,
    targetHeight: Int? = null
): Bitmap {
    val destWidth: Int
    val destHeight: Int
    if (targetWidth != null && targetHeight != null) {
        destWidth = targetWidth
        destHeight = targetHeight
    } else if (targetWidth != null) {
        destWidth = targetWidth
        destHeight = (targetWidth * src.height) / src.width
    } else if (targetHeight != null) {
        destWidth = (targetHeight * src.width) / src.height
        destHeight = targetHeight
    } else {
        destWidth = src.width
        destHeight = src.height
    }
    return Bitmap.createScaledBitmap(src, destWidth, destHeight, false)
}

@Suppress("unused")
fun resizeBitmap(
    src: Bitmap,
    maxSide: Int
): Bitmap {
    val targetHeight: Int?
    val targetWidth: Int?
    if (src.width > src.height) {
        targetWidth = maxSide
        targetHeight = null
    } else {
        targetWidth = null
        targetHeight = maxSide

    }
    return resizeBitmap(src, targetWidth, targetHeight)
}