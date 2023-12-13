//
//  PlaceViewModel.swift
//  Bubbles
//
//  Created by Петр Михеев on 13.12.2023.
//

import Foundation
import MapKit

class PlaceViewModel: ObservableObject {
    @Published var place: Place?

    func getPlace(id: String) {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().getPlace(id: id, accessToken: accessToken) { result in
            switch result {
            case .success(let res):
                DispatchQueue.main.async {
                    self.place = res
                }
            case .failure(let error):
                print("error here")
                print(error.localizedDescription)
            }
        }
    }
}

