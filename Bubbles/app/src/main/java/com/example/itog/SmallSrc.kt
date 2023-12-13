package com.example.itog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.yandex.mapkit.tiles.UrlProvider
import com.yandex.runtime.image.ImageProvider

class SmallSrc(private val context: Context) {

    fun getScaledImageProvider(drawable: Int): ImageProvider {
        var originalBitmap = BitmapFactory.decodeResource(context.resources, drawable)
        val scaledBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            (originalBitmap.width * 0.1f).toInt(),
            (originalBitmap.height * 0.1f).toInt(),
            true
        )
        return ImageProvider.fromBitmap(scaledBitmap)
    }
}