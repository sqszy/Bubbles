package com.example.itog

import MapSearchHelper
import Marker
import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.Serializable


data class Item(val id: Int, var title: String, var adres: String, var bitmap: Bitmap, var fragm: Int, var marker: Marker)
data class Otziv(val text: String, val rating: Float) : Serializable
data class myPhoto(val bitmap: Bitmap) : Serializable

class glav : AppCompatActivity(), UserLocationObjectListener{
    private lateinit var mapView: MapView
    private lateinit var adapter: adapterforcont
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationManager: LocationManager
    private lateinit var searchEdit: EditText
    private lateinit var locationuser: UserLocationLayer
    private lateinit var SearchmapObjects: MapObjectCollection
    private lateinit var AddMarkermapObjects: MapObjectCollection
    private var ListForCont = mutableListOf<Item>()
    private lateinit var oldContainer: FrameLayout
    private lateinit var locationHelper: LocationHelper
    private lateinit var storagePermissionHelper: StoragePermissionHelper
    private lateinit var uriphoto: Uri
    private var fragm = 1
    private var isSearching = false
    private var centerpoint = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_glav)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)

        storagePermissionHelper = StoragePermissionHelper(this)
        storagePermissionHelper.requestStoragePermission()
        val imageFileManager = ImageFileManager(this)
        val myView: View = findViewById(R.id.myview)
        mapView = findViewById(R.id.yandexMapView2)
        val mapKit: MapKit = MapKitFactory.getInstance()
        SearchmapObjects = mapView.map.mapObjects.addCollection()
        AddMarkermapObjects = mapView.map.mapObjects.addCollection()
        locationHelper = LocationHelper(this)
        requestLocationPermission()
        val db = Firebase.firestore
        oldContainer = findViewById(R.id.bottom_layout2)
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = adapterforcont(ListForCont, oldContainer, this, locationHelper)
        recyclerView.adapter = adapter



        //слой с меткой на местоположение
        locationuser = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationuser.isVisible = true

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationuser.setObjectListener(this)
        searchEdit = findViewById(R.id.search)
        val mapsearch = MapSearchHelper(mapView, SearchmapObjects)
        mapsearch.initializeSearch()
        searchEdit.setOnEditorActionListener{ v, actionID, event ->
            if(actionID == EditorInfo.IME_ACTION_SEARCH){
                mapsearch.submitQuery(searchEdit.text.toString())
                isSearching = true
            }
            false
        }

        val logout: ImageButton = findViewById(R.id.logout)
        logout.setOnClickListener {
            sp.edit().putString("activ", "офлайн").commit()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val resourceId: Int = resources.getIdentifier("bubbles", "drawable", packageName)
        uriphoto = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(resourceId) +
                "/" + resources.getResourceTypeName(resourceId) +
                "/" + resources.getResourceEntryName(resourceId)
        )

        //Обработка нажатий на карту
        val inputListener = object : InputListener {
            override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                // Handle single tap ...

                    // Изменяем положение метки на карте

            }

            override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
                // Handle long tap ...
            }
        }
        mapView.map.addInputListener(inputListener)
        updateLocation()


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Ваш код для работы с внешним хранилищем
        } else {
            // Если разрешения не предоставлены, запрашиваем у пользователя
            storagePermissionHelper.requestStoragePermission()
        }


        db.collection("places").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var counter = document.getDouble("id")?.toInt()
                    var title = document.getString("title")
                    var adres = document.getString("adres")
                    var bitmaparray = document.getString("bitmap")
                    var markerData = document["marker"] as? HashMap<*, *>
                    if (counter != null) {Counter.counter = counter}
                    var latitude = markerData?.get("targetPointLatitude") as? Double
                    var longitude = markerData?.get("targetPointLongitude") as? Double
                    // Проверка на null и дальнейшая обработка полученных данных
                    if (title != null && adres != null && bitmaparray != null && latitude != null && longitude != null) {

                        var point = Point(latitude, longitude)
                        var newMarker = Marker(this, AddMarkermapObjects, myView, adapter, mapsearch)
                        newMarker.addMarker(point)
                        newMarker.addzag(title)


                        val newItem = Item(
                            counter!!,
                            title,
                            adres,
                            imageFileManager.loadImageFromStorage(bitmaparray)!!,
                            fragm,
                            newMarker
                        )
                        adapter.addItem(newItem)
                    }
                }
            }
            .addOnFailureListener { e ->
            }










        ////////////////////////////////////////////////////////////////////////////////////////////

        val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedImageUri: Uri ->
                // Действия с выбранным URI фотографии
                uriphoto = selectedImageUri
                val Photo: ImageView = findViewById(R.id.photo12)
                val drawable: Drawable = ContextCompat.getDrawable(this,R.drawable.cornercont)!!
                Photo.background = drawable
                Photo.clipToOutline = true
                Photo.setImageURI(selectedImageUri)
                findViewById<CardView>(R.id.photoformaket).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.photo12).visibility = View.VISIBLE
            }
        }

        val pickPhoto2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedImageUri: Uri ->
                // Действия с выбранным URI фотографии
                uriphoto = selectedImageUri
                val Photo: ImageView = findViewById(R.id.photo123)
                val drawable: Drawable = ContextCompat.getDrawable(this,R.drawable.cornercont)!!
                Photo.background = drawable
                Photo.clipToOutline = true
                Photo.setImageURI(selectedImageUri)
                findViewById<CardView>(R.id.photoformaket2).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.photo123).visibility = View.VISIBLE
            }
        }


        val addMarker: ImageButton = findViewById(R.id.addplace)
        addMarker.setOnClickListener {
            if (centerpoint == false) {
                centerpoint = true
                findViewById<ImageView>(R.id.centerpoint).visibility = View.VISIBLE
            } else {
                centerpoint = false
                findViewById<ImageView>(R.id.centerpoint).visibility = View.GONE
                var counter = Counter.counter
                var newMarker = Marker(this, AddMarkermapObjects, myView, adapter, mapsearch)
                newMarker.addMarker(Point(0.0, 0.0))
                var coord = newMarker.getMarkerCoordinates()
                mapsearch.convertCoordinatesToAddress(coord.latitude, coord.longitude) { address ->
                    val adres = address
                    findViewById<FrameLayout>(R.id.addconteiner2).visibility = View.VISIBLE
                    val titleEditText: EditText = findViewById(R.id.zagadd2)
                    val YesButton: ImageButton = findViewById(R.id.yesbut2)
                    val NoButton: ImageButton = findViewById(R.id.nobut2)
                    val PhotoButton: ImageButton = findViewById(R.id.buttonaddphotocont1)
                    PhotoButton.setOnClickListener {
                        pickPhoto2.launch("image/*")
                    }
                    YesButton.setOnClickListener {
                        findViewById<FrameLayout>(R.id.addconteiner2).visibility = View.GONE
                        newMarker.addzag(titleEditText.text.toString())
                        val bitmap = getBitmapFromUri(uriphoto)
                        val newItem = Item(
                            counter,
                            titleEditText.text.toString(),
                            adres,
                            bitmap!!,
                            fragm,
                            newMarker
                        )


                        val place = hashMapOf(
                            "id" to counter,
                            "title" to titleEditText.text.toString(),
                            "adres" to adres,
                            "bitmap" to imageFileManager.saveImageToInternalStorage(bitmap),
                            "marker" to newMarker.toMap()
                        )
                        db.collection("places")
                            .add(place)
                            .addOnSuccessListener { documentReference ->
                                // Обработка успешного сохранения
                            }
                            .addOnFailureListener { e ->
                                // Обработка ошибки сохранения
                            }
                        adapter.addItem(newItem)
                        titleEditText.setText("")
                        findViewById<ImageView>(R.id.centerpoint).visibility = View.GONE
                        findViewById<CardView>(R.id.photoformaket).visibility = View.GONE
                        findViewById<ImageView>(R.id.photo123).visibility = View.GONE
                        uriphoto = Uri.parse(
                            ContentResolver.SCHEME_ANDROID_RESOURCE +
                                    "://" + resources.getResourcePackageName(resourceId) +
                                    "/" + resources.getResourceTypeName(resourceId) +
                                    "/" + resources.getResourceEntryName(resourceId)
                        )
                        titleEditText.setText("")
                    }
                    NoButton.setOnClickListener {
                        findViewById<FrameLayout>(R.id.addconteiner2).visibility = View.GONE
                        centerpoint = false
                        newMarker.deleteMarker()
                        findViewById<ImageView>(R.id.photo123).visibility = View.GONE
                        uriphoto = Uri.parse(
                            ContentResolver.SCHEME_ANDROID_RESOURCE +
                                    "://" + resources.getResourcePackageName(resourceId) +
                                    "/" + resources.getResourceTypeName(resourceId) +
                                    "/" + resources.getResourceEntryName(resourceId)
                        )
                        titleEditText.setText("")
                    }
                }
            }
        }




        val poisk: ImageButton = findViewById(R.id.poisk)
        poisk.setOnClickListener {
            findViewById<EditText>(R.id.search).requestFocus()
        }

        val addButton: Button = findViewById(R.id.dobavl)
        addButton.setOnClickListener {
            findViewById<RelativeLayout>(R.id.addconteiner).visibility = View.VISIBLE
            val titleEditText: EditText = findViewById(R.id.zagadd)
            val adresEditText: EditText = findViewById(R.id.adresadd)
            val YesButton: ImageButton = findViewById(R.id.yesbut)
            val NoButton: ImageButton = findViewById(R.id.nobut)
            val PhotoButton: ImageButton = findViewById(R.id.buttonaddphotocont)
            PhotoButton.setOnClickListener {
                pickPhoto.launch("image/*")
            }
            YesButton.setOnClickListener {
                mapsearch.convertAddressToCoordinates(adresEditText.text.toString()){ latitude, longitude ->
                    val point = Point(latitude, longitude)
                    var counter = Counter.counter
                    var newMarker = Marker(this, AddMarkermapObjects, myView, adapter, mapsearch)
                    newMarker.addMarker(point)
                    findViewById<RelativeLayout>(R.id.addconteiner).visibility = View.GONE
                    newMarker.addzag(titleEditText.text.toString())
                    val bitmap = getBitmapFromUri(uriphoto)
                    val newItem = Item(
                        counter,
                        titleEditText.text.toString(),
                        adresEditText.text.toString(),
                        bitmap!!,
                        fragm,
                        newMarker)
                    val place = hashMapOf(
                        "id" to counter,
                        "title" to titleEditText.text.toString(),
                        "adres" to adresEditText.text.toString(),
                        "bitmap" to imageFileManager.saveImageToInternalStorage(bitmap),
                        "marker" to newMarker.toMap()
                    )
                    db.collection("places")
                        .add(place)
                        .addOnSuccessListener { documentReference ->
                            // Обработка успешного сохранения
                        }
                        .addOnFailureListener { e ->
                            // Обработка ошибки сохранения
                        }
                    adapter.addItem(newItem)
                    adresEditText.setText("")
                    titleEditText.setText("")
                    findViewById<ImageView>(R.id.photo12).visibility = View.GONE
                    uriphoto = Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE +
                                "://" + resources.getResourcePackageName(resourceId) +
                                "/" + resources.getResourceTypeName(resourceId) +
                                "/" + resources.getResourceEntryName(resourceId)
                    )
                }
            }
            NoButton.setOnClickListener {
                findViewById<RelativeLayout>(R.id.addconteiner).visibility = View.GONE
                findViewById<ImageView>(R.id.photo12).visibility = View.GONE
                uriphoto = Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + resources.getResourcePackageName(resourceId) +
                            "/" + resources.getResourceTypeName(resourceId) +
                            "/" + resources.getResourceEntryName(resourceId)
                )
                titleEditText.setText("")
                adresEditText.setText("")

            }

        }


        val buttonZoomIn: ImageButton = findViewById(R.id.plus)
        buttonZoomIn.setOnClickListener {
            zoomIn()
        }

        val buttonZoomOut: ImageButton = findViewById(R.id.minus)
        buttonZoomOut.setOnClickListener {
            zoomOut()
        }

        //кнопка для обновления местоположения
        val UPloc: ImageButton = findViewById(R.id.like)
        UPloc.setOnClickListener {
            updateLocation()
        }
    }


    private fun requestLocationPermission() {
        locationHelper.requestLocationPermission(LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun updateLocation() {
        // Передаем mapView в метод updateLocation класса LocationHelper
        locationHelper.updateLocation(mapView)
    }

    //+ and - for zoom
    // Приближение карты
    private fun zoomIn() {
        mapView.map?.let { map ->
            val currentZoom = map.cameraPosition.zoom
            val newZoom = currentZoom + 1.0f // Увеличиваем текущий масштаб на 1
            val cameraPosition = CameraPosition(
                map.cameraPosition.target,
                newZoom,
                map.cameraPosition.azimuth,
                map.cameraPosition.tilt
            )
            map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, 0.25f),
                null
            )
        }
    }

    // Отдаление карты
    private fun zoomOut() {
        mapView.map?.let { map ->
            val currentZoom = map.cameraPosition.zoom
            val newZoom = currentZoom - 1.0f // Уменьшаем текущий масштаб на 1
            val cameraPosition = CameraPosition(
                map.cameraPosition.target,
                newZoom,
                map.cameraPosition.azimuth,
                map.cameraPosition.tilt
            )
            map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, 0.25f),
                null
            )
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }







    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationView.arrow.setIcon(SmallSrc(this).getScaledImageProvider(R.drawable.userloc))

        val picIcon = userLocationView.pin.useCompositeIcon()

        picIcon.setIcon("icon", SmallSrc(this).getScaledImageProvider(R.drawable.userloc), IconStyle().
        setAnchor(PointF(0f,0f)).setRotationType(RotationType.NO_ROTATION).setZIndex(0f).setScale(1f))

        picIcon.setIcon("pin", SmallSrc(this).getScaledImageProvider(R.drawable.strelka), IconStyle().
        setAnchor(PointF(0.5f,0.5f)).setRotationType(RotationType.NO_ROTATION).setZIndex(1f).setScale(1f))

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }





    override fun onBackPressed() {
        // Очищаем объекты на карте при нажатии кнопки "Назад"
        if (isSearching){
            SearchmapObjects.clear()
            isSearching = false
            searchEdit.setText("")}
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
    }



    override fun onDestroy() {
        mapView.onStop()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Передаем результат запроса разрешения в StoragePermissionHelper
        storagePermissionHelper.onRequestPermissionsResult(requestCode, grantResults)
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
    fun bitmapToByteArray(bitmap: Bitmap): String{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }

    fun byteArrayToBitmap(stringData: String): Bitmap {
        var byteArray = android.util.Base64.decode(stringData, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}

