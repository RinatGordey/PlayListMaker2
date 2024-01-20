package com.example.playlistmaker2

import android.content.Context
import android.util.TypedValue

object ConversionDpToPx {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
            ,dp
            ,context.resources.displayMetrics).toInt()
    }
}