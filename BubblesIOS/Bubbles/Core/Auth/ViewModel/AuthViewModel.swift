//
//  AuthViewModel.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import Foundation

@MainActor
class AuthViewModel: ObservableObject {
    var email: String = ""
    var password: String = ""
    var username: String = ""
    @Published var isAuth: Bool = false
    
    init() {
        checkAuthStatus()
    }
    
    func checkAuthStatus() {
        let defaults = UserDefaults.standard
        
        if defaults.string(forKey: "accessToken") != nil {
            
            isAuth = true
        } else {
            
            isAuth = false
        }
    }
    
    func login() {
        let defaults = UserDefaults.standard
        
        Webservice().login(email: email, password: password) { result in
            switch result {
            case .success(let authResponse):
                guard let accessToken = authResponse.accessToken,
                      let refreshToken = authResponse.refreshToken else {
                    // Обработка ошибки, если токены не были получены
                    return
                }
                
                defaults.setValue(accessToken, forKey: "accessToken")
                defaults.setValue(refreshToken, forKey: "refreshToken")
                DispatchQueue.main.async {
                    self.isAuth = true
                    self.checkAuthStatus()
                }
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }
    
    func register() {
        let defaults = UserDefaults.standard
        
        Webservice().register(email: email, username: username, password: password) { result in
            switch result {
            case .success(let authResponse):
                guard let accessToken = authResponse.accessToken,
                      let refreshToken = authResponse.refreshToken else {
                    // Обработка ошибки, если токены не были получены
                    return
                }
                
                defaults.setValue(accessToken, forKey: "accessToken")
                defaults.setValue(refreshToken, forKey: "refreshToken")
                DispatchQueue.main.async {
                    self.isAuth = true
                    self.checkAuthStatus()
                }
            case .failure(let error):
                print("error is here")
                print(error.localizedDescription)
            }
        }
    }
    
    
    func signOut() {
        let defaults = UserDefaults.standard
        
        defaults.removeObject(forKey: "accessToken")
        defaults.removeObject(forKey: "refreshToken")
        
        isAuth = false
    }
}
