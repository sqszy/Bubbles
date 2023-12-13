package com.example.itog

import AdapterforPhoto
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class InitialFragment(private var item: Item, private val context: Context, private var locationHelper: LocationHelper): Fragment() {
    private val PICK_IMAGES_REQUEST = 2
    private lateinit var adapterforPhoto: AdapterforPhoto
    private lateinit var viewPager: ViewPager2
    private var dataSetforPhoto = mutableListOf<myPhoto>()
    private var dataSetforOtziv = mutableListOf<Otziv>()
    private lateinit var adapterforotziv: adapterforotziv
    private lateinit var view: View
    private val mapView = (context as glav).findViewById<MapView>(R.id.yandexMapView2)
    private val marshrut = Marshrut(context, mapView,locationHelper.coord, item.marker.getMarkerCoordinates())
    private var RatBarON = true
    private var Photoadd = true
    private var MarshrutONorOFF = false
    private var photoList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейт макета фрагмента
        view = inflater.inflate(R.layout.page2, container, false)
        // Найдем FrameLayout внутри вашего фрагмента
        // Добавим содержимое в FrameLayout (если необходимо)
        viewPager = view.findViewById(R.id.viewPager)
        val db = Firebase.firestore
        // Создание адаптера и связывание его с ViewPager2
        adapterforPhoto = AdapterforPhoto(dataSetforPhoto, requireContext())
        viewPager.adapter = adapterforPhoto
        viewPager.setClipToPadding(false)
        if (Photoadd){
            adapterforPhoto.addPhoto(item.bitmap)
            Photoadd=false
        }

        if (dataSetforPhoto.isEmpty()){
            view.findViewById<FrameLayout>(R.id.addphotobutt).visibility = View.VISIBLE
            view.findViewById<FrameLayout>(R.id.addph).visibility = View.GONE
            view.findViewById<FrameLayout>(R.id.delph).visibility = View.GONE
        }
        else{view.findViewById<FrameLayout>(R.id.addphotobutt).visibility = View.GONE
            view.findViewById<FrameLayout>(R.id.addph).visibility = View.VISIBLE
            view.findViewById<FrameLayout>(R.id.delph).visibility = View.VISIBLE}





        val marsh: ImageButton = view.findViewById(R.id.marshrut)
        marsh.setOnClickListener {
            if (!MarshrutONorOFF){
                marshrut.buildRoute()
                MarshrutONorOFF = true
            }
            else{
                marshrut.clearRoute()
                marshrut.buildRoute()
                MarshrutONorOFF = true
            }
        }




        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
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
        val addPhoto: ImageButton = view.findViewById(R.id.buttonaddphoto)
        addPhoto.setOnClickListener {
            openGalleryForMultipleImages()
        }

        val addPhoto2: ImageButton = view.findViewById(R.id.addphotoimbutt)
        addPhoto2.setOnClickListener {
            openGalleryForMultipleImages()
        }

        val deletephoto: ImageButton = view.findViewById(R.id.deletephoto)
        deletephoto.setOnClickListener {
            adapterforPhoto.removePhoto()
            photoList = mutableListOf<String>()
            db.collection("photos")
                .whereEqualTo("id", item.id)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val docRef = db.collection("photos").document(document.id)
                        docRef.delete()
                            .addOnSuccessListener {
                                // Обработка успешного удаления документа
                            }
                            .addOnFailureListener { e ->
                                // Обработка ошибки удаления
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Ошибка при выполнении запроса
                }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerotziv)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapterforotziv = adapterforotziv(dataSetforOtziv)
        recyclerView.adapter = adapterforotziv

        if(RatBarON){
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
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
            ratingBar.setIsIndicator(true)
            RatBarON = false
        }}
        update(item)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        val imageFileManager = ImageFileManager(context)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    val bitmap = getBitmapFromUri(imageUri)
                    if (bitmap != null){
                        adapterforPhoto.addPhoto(bitmap)
                        photoList.add(imageFileManager.saveImageToInternalStorage(bitmap)!!)}

                }
            } else if (data.data != null) {
                val imageUri: Uri = data.data!!
                val bitmap = getBitmapFromUri(imageUri)
                if (bitmap != null){
                    adapterforPhoto.addPhoto(bitmap)
                    photoList.add(imageFileManager.saveImageToInternalStorage(bitmap)!!)}


            }
            adapterforPhoto.updateDataSet(dataSetforPhoto)
        }
        view?.findViewById<FrameLayout>(R.id.addphotobutt)?.visibility = View.GONE
        view?.findViewById<FrameLayout>(R.id.addph)?.visibility = View.VISIBLE
        view?.findViewById<FrameLayout>(R.id.delph)?.visibility = View.VISIBLE

    }

    private fun onBackPressed() {
        if(MarshrutONorOFF){
            marshrut.clearRoute()
            MarshrutONorOFF = false
        }
        else{
            val oldContainer = requireActivity().findViewById<FrameLayout>(R.id.bottom_layout2)
            oldContainer.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStack()

        }
    }
    fun update(item: Item) {
        view.findViewById<TextView>(R.id.Titlemak).text = item.title
        view.findViewById<TextView>(R.id.Adresmak).text = item.adres
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        val db = Firebase.firestore
        if(!photoList.isEmpty()){
        val phototosave = hashMapOf(
            "id" to item.id,
            "photos" to photoList
        )
        db.collection("photos")
            .whereEqualTo("id", item.id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Документ с указанным значением id не найден, можно создать новый
                    db.collection("photos")
                        .add(phototosave)
                        .addOnSuccessListener { documentReference ->
                            // Обработка успешного сохранения
                        }
                        .addOnFailureListener { e ->
                            // Обработка ошибки сохранения
                        }
                } else {
                    for (document in documents) {
                        val docRef = db.collection("photos").document(document.id)
                        docRef.set(phototosave)
                            .addOnSuccessListener {
                                // Обработка успешного обновления документа
                            }
                            .addOnFailureListener { e ->
                                // Обработка ошибки сохранения
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Ошибка при выполнении запроса
            }
        }
        photoList = mutableListOf<String>()
        adapterforPhoto.removePhoto()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val db = Firebase.firestore
        val imageFileManager = ImageFileManager(context)
        db.collection("photos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var id = document.getDouble("id")?.toInt()
                    if(id == item.id){
                        val photo = document.get("photos") as? List<String>
                        if(photo!==null){
                            for(el in photo){
                                if (!photoList.contains(el)) {
                                    adapterforPhoto.addPhoto(imageFileManager.loadImageFromStorage(el)!!)
                                    if (!photoList.contains(el)) {
                                        photoList.add(el)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Обработка ошибки при получении данных
            }
    }


}