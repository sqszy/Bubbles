//
//  HomeMapViewRepresentable.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI
import MapKit

struct HomeMapViewRepresentable: UIViewRepresentable {
    let mapView = MKMapView()
    let locationManager = LocationManager()
    var places: [Place] = [
        Place(
            _id: "65764a139f10c276dc5b8b32",
            title: "Исаакиевский",
            about: "Крутой собор",
            latitude: 54.748803,
            longitude: 55.974601,
            creatorId: "65763eb0a4c260cf9c407e9f",
            images: [],
            reviews: [],
            tags: [
                "65763ef644a5f6a18654c712",
                "65763ef644a5f6a18654c713",
                "65763ef644a5f6a18654c714"
            ]
        )
        // Добавьте другие объекты Place здесь, если это необходимо
    ]
    
    func makeUIView(context: Context) -> some UIView {
        mapView.delegate = context.coordinator
        mapView.isRotateEnabled = false
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .follow
        
        return mapView
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        guard let annotation = annotation as? MKPointAnnotation else { return nil }
        
        let identifier = "CustomAnnotation"
        var annotationView: MKAnnotationView
        
        if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier) {
            annotationView = dequeuedView
        } else {
            annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            annotationView.canShowCallout = true // Отображение балуна с информацией
            annotationView.calloutOffset = CGPoint(x: -5, y: 5) // Смещение балуна относительно аннотации
            annotationView.rightCalloutAccessoryView = UIButton(type: .detailDisclosure) // Добавление кнопки в балун
        }
        
        // Установка изображения для аннотации
        annotationView.image = UIImage(systemName: "multiply.circle.fill") // Укажите имя изображения для вашей кастомной иконки
        
        return annotationView
    }



    
    func updateUIView(_ uiView: UIViewType, context: Context) {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().getPlaces(accessToken: accessToken) { result in
            switch result {
            case .success(let places):
                DispatchQueue.main.async {
                    mapView.removeAnnotations(mapView.annotations)
                    addAnnotations(places: places)
                } 
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }

    
    func makeCoordinator() -> MapCoordinator {
        return MapCoordinator(parent: self)
    }
    
    func addAnnotations(places: [Place]) {
        for place in places {
            let annotation = MKPointAnnotation()
            annotation.coordinate = CLLocationCoordinate2D(latitude: place.latitude, longitude: place.longitude)
            annotation.title = place.title
            annotation.subtitle = place.about
            mapView.addAnnotation(annotation)
        }
    }
    
}


extension HomeMapViewRepresentable {
    class MapCoordinator: NSObject, MKMapViewDelegate {
        let parent: HomeMapViewRepresentable
        
        init(parent: HomeMapViewRepresentable) {
            self.parent = parent
            super.init()
        }
        
        func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
            let region = MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude, longitude: userLocation.coordinate.longitude),
                span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
            )
            parent.mapView.setRegion(region, animated: true)
        }
    }
}
