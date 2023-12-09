//
//  LoginView.swift
//  Bubbles
//
//  Created by Петр Михеев on 09.12.2023.
//

import SwiftUI

struct LoginView: View {
    @State private var email = ""
    @State private var password = ""
    @EnvironmentObject var authVM: AuthViewModel
    
    var body: some View {
            NavigationStack {
                ZStack {
                    Color.black.edgesIgnoringSafeArea(/*@START_MENU_TOKEN@*/.all/*@END_MENU_TOKEN@*/)
                    VStack(spacing: 12) {
                        // welcome text
                        
                        GlowyTextView(text: "ДОБРО ПОЖАЛОВАТЬ", colors: [.blue, .red], fontSize: 24).padding(.top, 80).padding(.bottom, 40)
                        
                        
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
                        
                        NavigationLink {
                            RegistrationView()
                                .navigationBarBackButtonHidden()
                            
                        } label: {
                            HStack(spacing: 4) {
                                Text("Нет аккаунта?")
                                Text("Зарегистрироваться")
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
    LoginView()
}
