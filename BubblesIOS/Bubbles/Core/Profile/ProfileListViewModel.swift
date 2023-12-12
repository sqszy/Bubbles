//
//  ProfileListViewModel.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import Foundation

class ProfileListViewModel: ObservableObject {
    @Published var user: User? // Отключаем необязательное значение, так как оно может быть nil
    
    func uploadAvatarImage(imageData: Data) {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().uploadAvatar(accessToken: accessToken, imageData: imageData) { result in
            switch result {
            case .success:
                // Обработка успешной загрузки изображения, если это необходимо
                print("Image uploaded successfully!")
            case .failure(let error):
                print("Error uploading image: \(error.localizedDescription)")
            }
        }
    }
    
    func getUser() {
        let defaults = UserDefaults.standard
        
        guard let accessToken = defaults.string(forKey: "accessToken") else {
            return
        }
        
        Webservice().fetchUserInfo(accessToken: accessToken) { result in
            switch result {
            case .success(let user):
                DispatchQueue.main.async {
                    self.user = user // Присваиваем полученные данные пользователю
                }
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }
}

