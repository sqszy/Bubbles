//
//  SearchViewModel.swift
//  Bubbles
//
//  Created by Петр Михеев on 12.12.2023.
//

import Foundation

class SearchViewModel: ObservableObject {
    @Published var places: [Place]?
    var query: String = ""
    
    func search(query: String) {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().searchPlace(query: query, tags: [""], accessToken: accessToken) { result in
            switch result {
            case .success(let res):
                DispatchQueue.main.async {
                    self.places = res // Присваиваем полученные данные пользователю
                }
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }
}
