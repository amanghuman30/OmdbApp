package com.omdb.app.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.omdb.app.R

class InfoChip(context: Context, attr: AttributeSet? = null) : Chip(context, attr) {

    init {
        chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.black_gen)
        chipStrokeWidth = context.resources.getDimension(R.dimen.chip_stroke_width)
        setTextColor(ContextCompat.getColor(context,R.color.chip_info_grey))

        chipStrokeColor = ContextCompat.getColorStateList(context, R.color.chip_info_grey)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)

    }

}