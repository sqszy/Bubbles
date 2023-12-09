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
                    
                    GlowyTextView(text: "ДОБРО ПОЖАЛОВАТЬ", colors: [.blue, .red], fontSize: 24).padding(.top, 80).padding(.bottom, 40)
                    
                    Image(systemName: authVM.isAuth ? "lock.open" : "lock.fill").foregroundColor(.white)
                    
                    // inputs
                    
                    VStack(spacing: 24) {
                        InputView(
                            text: $authVM.email,
                            placeholder: "name@example.com")
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        
                        InputView(text: $authVM.password, placeholder: "Пароль", isSecureField: true)
                    }
                    
                    HStack {
                        // login
                        Button {
                            print("loggin in...")
                            authVM.login()
                        } label: {
                            Text("Войти")
                            .font(.system(size: 12))
                            .foregroundColor(.white)
                            .padding(.vertical, 10)
                            .padding(.horizontal, 20)
                        }
                        .background(Color(.systemBlue))
                        .cornerRadius(12)
                        
                        // forgot pass
                        
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
                }
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
            }
        }
    }
}
}

#Preview {
    RegistrationView()
}
