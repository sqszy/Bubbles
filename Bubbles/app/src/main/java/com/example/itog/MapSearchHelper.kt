import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.itog.R
import com.example.itog.SmallSrc
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class MapSearchHelper(private val mapView: MapView,  var SearchmapObjects: MapObjectCollection) : com.yandex.mapkit.search.Session.SearchListener {
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: com.yandex.mapkit.search.Session

    fun initializeSearch() {
        SearchFactory.initialize(mapView.context)
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    fun submitQuery(query: String) {
        searchSession = searchManager.submit(query, VisibleRegionUtils.toPolygon(mapView.map.visibleRegion), SearchOptions(), this)
    }

    override fun onSearchResponse(response: Response) {
        SearchmapObjects.clear()
        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj?.geometry?.get(0)?.point
            resultLocation?.let {
                SearchmapObjects.addPlacemark(
                    it,
                    SmallSrc(mapView.context).getScaledImageProvider(R.drawable.location)
                )
            }
        }
    }

    override fun onSearchError(error: com.yandex.runtime.Error) {
        var errorMessage="Неизвестная ошибка!"
        if(error is RemoteError){
            errorMessage="Ошибка"
        }else if(error is NetworkError){
            errorMessage="Проблема с интернетом!"
        }
        Toast.makeText(mapView.context,errorMessage, Toast.LENGTH_SHORT)
    }

    fun convertAddressToCoordinates(address: String, callback: (Double, Double) -> Unit) {
        val searchSession = searchManager.submit(
            address,
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            object : com.yandex.mapkit.search.Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val firstResult = response.collection.children.firstOrNull()
                    val point = firstResult?.obj?.geometry?.get(0)?.point
                    point?.let {
                        callback.invoke(it.latitude, it.longitude)
                    }
                }

                override fun onSearchError(error: com.yandex.runtime.Error) {
                    // При возникновении ошибки ничего не делать
                }
            }
        )
    }

    fun convertCoordinatesToAddress(latitude: Double, longitude: Double, callback: (String) -> Unit) {
        val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        val point = com.yandex.mapkit.geometry.Point(latitude, longitude)

        val searchSession = searchManager.submit(
            "",
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            object : com.yandex.mapkit.search.Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val firstResult = response.collection.children.firstOrNull()
                    val address = firstResult?.obj?.descriptionText ?: ""
                    callback.invoke(address)
                }

                override fun onSearchError(error: com.yandex.runtime.Error) {
                    // Обработка ошибки обратного геокодирования
                    callback.invoke("")
                }
            }
        )
    }
}
