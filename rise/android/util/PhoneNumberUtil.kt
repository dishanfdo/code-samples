package com.example.app.apputil

import android.content.Context
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.util.*

lateinit var lib: PhoneNumberUtil

object PhoneNumberUtil {
    fun initialize(context: Context) {
        lib = PhoneNumberUtil.createInstance(context)
    }
}

private fun toE164FormatOrNull(number: String, region: String? = null): String? {
    return try {
        val userRegion: String? = region ?: Locale.getDefault().country
        val parsed = lib.parse(number, userRegion)
        lib.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
    } catch (ex: NumberParseException) {
        return null
    }
}

/**
 * Convert the phone number represented by this string to E164 format. Return the number without
 * any formatting if the formatting fails
 */
fun String.toE164OrSame(region: String? = null): String {
    return this.toE164OrNull(region) ?: this
}

/**
 * Convert the phone number represented by this string to E164 format. Return null if the
 * formatting fails
 */
fun String.toE164OrNull(region: String? = null): String? {
    return toE164FormatOrNull(this, region)
}




