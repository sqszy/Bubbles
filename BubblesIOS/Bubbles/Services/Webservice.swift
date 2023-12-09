//
//  Webservice.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import Foundation

enum AuthenticationError: Error {
    case invalidCredentials
    case custom(errorMessage: String)
}

struct LoginRequestBody: Codable {
    let email: String
    let password: String
}

struct LoginResponse: Codable {
    let accessToken: String?
    let refreshToken: String?
    let user: User?
}

struct User: Codable {
    let email: String
    let id: String
    let isActivated: Bool
    let username: String
    let registeredAt: String
    let isAdmin: Bool
    let image: String
}

class Webservice {
    func login(email: String, password: String, completion: @escaping (Result<String, AuthenticationError>) -> Void) {
        
        guard let url = URL(string: "http://localhost:8080/api/auth/login") else {
            completion(.failure(.custom(errorMessage: "URL is not correct")))
            return
        }
        
        let body = LoginRequestBody(email: email, password: password)
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONEncoder().encode(body)
        
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            guard let data = data, error == nil else {
                completion(.failure(.custom(errorMessage: "No data")))
                return
            }
            
            guard let loginResponse = try? JSONDecoder().decode(LoginResponse.self, from: data) else {
                completion(.failure(.invalidCredentials))
                return
            }
            
            guard let accessToken = loginResponse.accessToken else {
                completion(.failure(.invalidCredentials))
                return
            }
            
            completion(.success(accessToken))
            
        }.resume()
    }
    
    func signOut() {
        
    }
}
