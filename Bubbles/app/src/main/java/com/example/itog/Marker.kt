import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.itog.Counter
import com.example.itog.R
import com.example.itog.SmallSrc
import com.example.itog.adapterforcont
import com.example.itog.glav
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class Marker(private val context: Context, private var AddMarkermapObjects: MapObjectCollection, private val view: View, private val adapter: adapterforcont, private val mapsearch: MapSearchHelper) {
    private var mapView: MapView
    private lateinit var MarkermapObject: PlacemarkMapObject
    private lateinit var targetPoint: Point

    init {
        mapView = view.findViewById(R.id.yandexMapView2)
    }
    fun addMarker(point: Point) {
        if (point.latitude==0.0 && point.longitude==0.0) {
            val mapCenter = mapView.map.cameraPosition.target
            targetPoint = Point(mapCenter.latitude, mapCenter.longitude)
        } else {
            targetPoint = point
        }


        MarkermapObject = AddMarkermapObjects.addPlacemark(targetPoint, SmallSrc(context).getScaledImageProvider(R.drawable.bubbles))
        MarkermapObject.isDraggable = true
        MarkermapObject.setDragListener(dragListener)
        MarkermapObject.addTapListener(placemarkTapListener)
        MarkermapObject.userData = Counter.counter
        Counter.counter++
    }

    private val placemarkTapListener = MapObjectTapListener { mapobject, point ->
        val YesOrNoyDel: FrameLayout = view.findViewById(R.id.yesornordel)
        YesOrNoyDel.visibility = View.VISIBLE
        val yesdel: ImageButton = view.findViewById(R.id.Yesdel)
        Toast.makeText(context, "${MarkermapObject.userData as Int} ", Toast.LENGTH_SHORT).show()
        yesdel.setOnClickListener {
            YesOrNoyDel.visibility = View.GONE
            adapter.removeItem(MarkermapObject.userData as Int)
        }
        val nodel: ImageButton = view.findViewById(R.id.Nodel)
        nodel.setOnClickListener {
            YesOrNoyDel.visibility = View.GONE
        }
        true
    }
    val dragListener = object : MapObjectDragListener {
        var startPoint: Point? = null // Точка начала перетаскивания
        override fun onMapObjectDragStart(p0: MapObject) {
            startPoint = MarkermapObject.geometry // Фиксируем начальную точку перетаскивания
        }

        override fun onMapObjectDrag(mapObject: MapObject, point: Point) {
            view.findViewById<TextView>(R.id.coordinate).visibility = View.VISIBLE
            var latitude: Double = point.latitude
            var longitude: Double = point.longitude
            val coordinates = "Latitude:" + "%.5f".format(latitude) + " Longitude:" + "%.5f".format(longitude)
            view.findViewById<TextView>(R.id.coordinate).setText(coordinates)
        }

        override fun onMapObjectDragEnd(mapobj: MapObject) {
            val YesOrNoy: FrameLayout = view.findViewById(R.id.yesornor)
            YesOrNoy.visibility = View.VISIBLE
            val yes: ImageButton = view.findViewById(R.id.YesB)
            yes.setOnClickListener {
                YesOrNoy.visibility = View.GONE
                mapsearch.convertCoordinatesToAddress(MarkermapObject.geometry.latitude, MarkermapObject.geometry.longitude){ address ->
                    var adres = address
                    adapter.newadres(MarkermapObject.userData as Int, adres)
                    Toast.makeText(context, "${adres}", Toast.LENGTH_SHORT).show()
                }
            }
            val no: ImageButton = view.findViewById(R.id.NoB)
            no.setOnClickListener {
                MarkermapObject.geometry = startPoint ?: Point(0.0, 0.0)
                YesOrNoy.visibility = View.GONE
            }
            view.findViewById<TextView>(R.id.coordinate).visibility = View.GONE
        }

    }
    fun getMarkerCoordinates(): Point {
        return MarkermapObject.geometry
    }

    fun addzag(nameplace: String){
        MarkermapObject.setText(
            nameplace,
            TextStyle( 8f, // Размер текста (Float)
                Color.BLACK, // Цвет текста (Int?)
                Color.WHITE, // Цвет обводки текста (Int?)
                TextStyle.Placement.BOTTOM, // Расположение текста
                1.0f, // Смещение текста относительно иконки
                true, // Смещение текста относительно иконки (Boolean)
                false // Может быть пустой (Boolean)
            )
        )
    }
    fun deleteMarker() {
        AddMarkermapObjects.remove(MarkermapObject)
    }

    fun toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        // Добавьте в map необходимые данные для сохранения в Firebase
        map["targetPointLatitude"] = targetPoint.latitude
        map["targetPointLongitude"] = targetPoint.longitude
        // Добавьте другие данные объекта, которые вам нужно сохранить
        return map
    }
}
