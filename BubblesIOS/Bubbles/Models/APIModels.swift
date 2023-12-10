//
//  APIModels.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import Foundation

enum AuthenticationError: Error {
    case invalidCredentials
    case custom(errorMessage: String)
    case invalidURL
    case NoData
    case decodingError
}

enum RegistrationError: Error {
    case emailExists
    case custom(errorMessage: String)
    case serverError(description: String)
}

struct LoginRequestBody: Codable {
    let email: String
    let password: String
}

struct AuthResponse: Codable {
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
    let image: String?
}

struct RegisterRequestBody: Codable {
    let email: String
    let username: String
    let password: String
}
