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
    @Published var isAuth: Bool = false
    
    init() {
        checkAuthStatus()
    }
    
    func checkAuthStatus() {
        let defaults = UserDefaults.standard
        
        if let token = defaults.string(forKey: "jsonwebtoken") {
            // Если токен сохранен, пользователь аутентифицирован
            isAuth = true
        } else {
            // Если нет, пользователь не аутентифицирован
            isAuth = false
        }
    }
    
    func login() {
        let defaults = UserDefaults.standard
        
        Webservice().login(email: email, password: password) { result in
            switch result {
            case .success(let token):
                defaults.setValue(token, forKey: "jsonwebtoken")
                DispatchQueue.main.async {
                    self.isAuth = true
                    self.checkAuthStatus()
                }
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
    }
    
    func signOut() {
        let defaults = UserDefaults.standard
        
        // Удаляем сохраненный токен "jsonwebtoken" из UserDefaults
        defaults.removeObject(forKey: "jsonwebtoken")
        
        // Обнуляем статус аутентификации в AuthViewModel
        isAuth = false
    }
}
