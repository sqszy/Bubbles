//
//  RegistrationView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct RegistrationView: View {
    @EnvironmentObject var authVM: AuthViewModel
    @Environment(\.dismiss) var dismiss
    var body: some View {
        NavigationStack {
            ZStack {
                Color.black.edgesIgnoringSafeArea(/*@START_MENU_TOKEN@*/.all/*@END_MENU_TOKEN@*/)
                VStack(spacing: 12) {
                    // welcome text
                    
                    GlowyTextView(text: "ДОБРО ПОЖАЛОВАТЬ", colors: [
                        Color(red: 66/255, green: 164/255, blue: 1),
                        Color(red: 71/255, green: 255/255, blue: 255/255)
                    ], fontSize: 24).padding(.top, 80).padding(.bottom, 40)
                    
                    
                    // inputs
                    
                    VStack(spacing: 12) {
                        InputView(
                            text: $authVM.email,
                            placeholder: "name@example.com")
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        
                        InputView(
                            text: $authVM.username,
                            placeholder: "Ваше имя пользователя")
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        
                        InputView(text: $authVM.password, placeholder: "Ваш пароль", isSecureField: true)
                    }
                    
                    HStack {
                        // login
                        Button {
                            print("registering and logging in...")
                            authVM.register()
                        } label: {
                            Text("Регистрация")
                                .font(.system(size: 12))
                                .foregroundColor(.black)
                                .fontWeight(.medium)
                                .padding(.vertical, 10)
                                .padding(.horizontal, 20)
                        }
                        .background(LinearGradient(colors: [
                            Color(red: 66/255, green: 164/255, blue: 1),
                            Color(red: 71/255, green: 255/255, blue: 255/255)
                        ], startPoint: .leading, endPoint: .leading))
                        .cornerRadius(12)
                        
                        // forgot pass
                        
                        Spacer()
                        
                        Button {
                            print("forgot password")
                        } label: {
                            Text("забыли пароль?")
                                .font(.system(size: 12))
                                .foregroundColor(.white)
                                .padding(.vertical, 10)
                                .padding(.horizontal, 20)
                        }
                        .cornerRadius(12)
                    }.padding(.top, 40)
                    Spacer()
                    
                    Button {
                        dismiss()
                        
                    } label: {
                        HStack(spacing: 4) {
                            Text("Уже есть аккаунт?")
                            Text("Войти")
                                .fontWeight(.bold)
                        }
                        .font(.system(size: 12))
                        .foregroundColor(.white)
                    }
                }.padding(.horizontal, 20)
            }
        }
    }
}

#Preview {
    RegistrationView()
}
