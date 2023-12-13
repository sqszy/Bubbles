//
//  HomeMapViewModel.swift
//  Bubbles
//
//  Created by Петр Михеев on 13.12.2023.
//

import Foundation
import MapKit

class HomeMapViewModel: ObservableObject {
    @Published var places: [Place]?
    @Published var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 0, longitude: 0),
        span: MKCoordinateSpan(latitudeDelta: 1, longitudeDelta: 1)
    )

    func getPlaces() {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().getPlaces(accessToken: accessToken) { result in
            switch result {
            case .success(let res):
                DispatchQueue.main.async {
                    self.places = res
                    self.updateMapRegion() // Обновляем область карты при получении мест
                }
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }

    private func updateMapRegion() {
        if let places = places, let firstPlace = places.first {
            let coordinate = CLLocationCoordinate2D(latitude: firstPlace.latitude, longitude: firstPlace.longitude)
            region = MKCoordinateRegion(center: coordinate, span: MKCoordinateSpan(latitudeDelta: 0.5, longitudeDelta: 0.5))
        }
    }
}
