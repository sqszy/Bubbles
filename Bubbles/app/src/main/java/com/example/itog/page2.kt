package com.example.itog

import AdapterforPhoto
import android.R.attr.text
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.grpc.Context
import io.grpc.Contexts
import java.io.IOException


import java.io.Serializable


class page2 : AppCompatActivity() {
    private val PICK_IMAGES_REQUEST = 2
    private lateinit var adapterforPhoto: AdapterforPhoto
    private lateinit var viewPager: ViewPager2
    private var dataSetforPhoto = mutableListOf<myPhoto>()
    private lateinit var adapterforotziv: adapterforotziv
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)
        viewPager = findViewById(R.id.viewPager)



        // Создание адаптера и связывание его с ViewPager2
        adapterforPhoto = AdapterforPhoto(dataSetforPhoto, this)
        viewPager.adapter = adapterforPhoto
        viewPager.setClipToPadding(false)



        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.setPageTransformer { page, position ->
            val offset = position * -((110 * resources.displayMetrics.density).toInt())
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.translationX = offset
            } else {
                page.translationY = offset
            }
        }
        val addPhoto: ImageButton = findViewById(R.id.buttonaddphoto)
        addPhoto.setOnClickListener {
            openGalleryForMultipleImages()
        }

        val addPhoto2: ImageButton = findViewById(R.id.addphotoimbutt)
        addPhoto2.setOnClickListener {
            openGalleryForMultipleImages()
        }

        val deletephoto: ImageButton = findViewById(R.id.deletephoto)
        deletephoto.setOnClickListener {
           adapterforPhoto.removePhoto()
            findViewById<FrameLayout>(R.id.addphotobutt).visibility = View.VISIBLE
            findViewById<FrameLayout>(R.id.addph).visibility = View.GONE
            findViewById<FrameLayout>(R.id.delph).visibility = View.GONE
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerotziv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterforotziv = adapterforotziv(mutableListOf())
        recyclerView.adapter = adapterforotziv

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Ваш код при изменении оценки
            val imageResource = when {
                rating == 1.0f -> R.drawable.star1
                rating == 2.0f -> R.drawable.star2
                rating == 3.0f -> R.drawable.star3
                rating == 4.0f -> R.drawable.star4
                rating == 5.0f -> R.drawable.star5
                else -> R.drawable.staroff
            }

            var newOtziv = Otziv("отзыв" , rating)
            adapterforotziv.addItem(newOtziv)
        }

    }
    private fun openGalleryForMultipleImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST)
    }
    // Метод для обработки выбора изображения
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    val bitmap = getBitmapFromUri(imageUri)
                    if (bitmap != null){
                        adapterforPhoto.addPhoto(bitmap)}
                }
            } else if (data.data != null) {
                val imageUri: Uri = data.data!!
                val bitmap = getBitmapFromUri(imageUri)
                if (bitmap != null){
                    adapterforPhoto.addPhoto(bitmap)}
            }
            adapterforPhoto.updateDataSet(dataSetforPhoto)
        }
        findViewById<FrameLayout>(R.id.addphotobutt).visibility = View.GONE
        findViewById<FrameLayout>(R.id.addph).visibility = View.VISIBLE
        findViewById<FrameLayout>(R.id.delph).visibility = View.VISIBLE
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = this.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
