package com.example.itog

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error


class Marshrut(private val context: Context, private val mapView: MapView, private var startcoord: Point, private var endcoord: Point) {
    private var drivingSession: DrivingSession? = null
    private var lines: MutableList<PolylineMapObject> = mutableListOf()
    fun buildRoute() {
        val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        val drivingOptions = DrivingOptions().apply {
            routesCount = 2
        }
        val vehicleOptions = VehicleOptions()

        val points = buildList {
            add(RequestPoint(startcoord, RequestPointType.WAYPOINT, null, null))
            add(RequestPoint(endcoord, RequestPointType.WAYPOINT, null, null))
        }

        val drivingSession = drivingRouter.requestRoutes(
            points,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
    }



    private val drivingRouteListener = object : DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            // Обработка успешного запроса маршрутов
            if (drivingRoutes.isNotEmpty()) {
                val mapObjects = mapView.map.mapObjects
                for (route in drivingRoutes) {
                    val geometry = route.geometry
                    val line = mapObjects.addPolyline(geometry)
                    // Настройка стиля линии маршрута, если требуется
                    line.setStrokeColor(Color.GREEN)
                    line.strokeWidth = 2f
                    lines.add(line)
                }
            }
        }

        override fun onDrivingRoutesError(error: Error) {
            // Обработка ошибки запроса маршрутов
            Log.e("RouteError", "Error getting routes: $error")
        }
    }
    private fun clearRoutes() {
        lines.forEach {
            mapView.map.mapObjects.remove(it)
        }
        lines.clear()
    }

    fun clearRoute() {
        clearRoutes()
        drivingSession?.cancel()
    }

}