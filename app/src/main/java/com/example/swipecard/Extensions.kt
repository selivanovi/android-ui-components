package com.example.swipecard

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View

fun View.animTranslationY(value: Float): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "translationY", value)


fun View.animTranslationX(value: Float): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "translationX", value)

fun decodeSampledBitmapFromResource(
    res: Resources,
    resId: Int,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false
    }
    return BitmapFactory.decodeResource(res, resId, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

val listOfDrawables = listOf<Int>(
    R.drawable.cool_emoji,
    R.drawable.dislike_emoji,
    R.drawable.happy_emoji,
    R.drawable.kiss_emoji,
    R.drawable.laugh_emoji,
    R.drawable.love_emoji,
    R.drawable.puke_emoji,
    R.drawable.sad_emoji,
    R.drawable.sick_emoji,
    R.drawable.sleep_emoji,
    R.drawable.smile_emoji,
    R.drawable.snore_emoji

)

