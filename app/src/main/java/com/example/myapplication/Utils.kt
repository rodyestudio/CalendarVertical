package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat


internal fun getcolor(
    typedArray: TypedArray, resourceId: Int, res: Resources,default:Int
): Int {
    return  try {
        val font =
            typedArray.getColor(resourceId,default)
        Log.e("iLOG","SUCCESS LOAD $font")
        font
    }catch (e:Exception){
        Log.e("iLOG","FAILED TO LOAD color ")
        default
    }
}

internal fun getDimension(typedArray: TypedArray, resourceId: Int, res: Resources,default:Float): Float {
    return  try {
        val font =
            typedArray.getDimension(resourceId,default)
        Log.e("iLOG","SUCCESS LOAD $font")
        font.toFloat()
    }catch (e:Exception){
        Log.e("iLOG","FAILED TO LOAD dim ")
        default
    }
}

internal fun getFont(context: Context, typedArray: TypedArray, resourceId: Int): Typeface? {
    return  try {
       val font = ResourcesCompat.getFont(context, typedArray.getResourceId(resourceId, -1))
        Log.e("iLOG","SUCCESS LOAD $font")
        font
    }catch (e:Exception){
        Log.e("iLOG","FAILED TO LOAD font ")
        null
    }
}

internal fun dpToPx(context: Context, dp: Int): Int {
    val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

internal  fun spToPx(sp: Int, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )
}
