//
//  ContentView.swift
//  Bubbles
//
//  Created by Петр Михеев on 19.09.2023.
//

import SwiftUI

struct CustomButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .fontWeight(.medium)
            .font(.system(size: 12))
            .lineSpacing(16)
            .lineLimit(1)
            .padding(.horizontal, 20.0)
            .padding(.vertical, 10.0)
//                        .frame(maxWidth: .infinity)
            .background(LinearGradient(
                gradient: Gradient(
                    colors: [
                        Color(red: 66/255, green: 164/255, blue: 255/255), // 0x42A4FF
                        Color(red: 71/255, green: 255/255, blue: 255/255) // 0x47FFFF
                    ]
                ),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            ))
            .foregroundColor(.black)
            .cornerRadius(10)
    }
}


struct ContentView: View {
    var welcome = "ДОБРО ПОЖАЛОВАТЬ"
    @State private var username = ""
    @State private var password = ""
    var body: some View {
        VStack(spacing: 40) {
            VStack(spacing: 10) {
                TextField("Логин", text: $username)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                
                SecureField("Пароль", text: $password)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
            }
            
            HStack(spacing: 10) {
                Button(action: {
                    // Действие, которое будет выполнено при нажатии на кнопку "Логин"
                    print("Логин: \(username)")
                    print("Пароль: \(password)")
                }) {
                    Text("Вход")
                        .fontWeight(.medium)
                        .font(.system(size: 12))
                        .lineSpacing(16)
                        .padding(.horizontal, 20.0)
                        .padding(.vertical, 10.0)
                        .background(Color.white)
                        .foregroundColor(.black)
                        .cornerRadius(10)
                }
                
                Button(action: {
                    // Действие, которое будет выполнено при нажатии на кнопку "Пароль"
                    print("Создать аккаунт")
                }) {
                    Text("Регистрация")
                }.buttonStyle(CustomButtonStyle())
                
                Spacer()
                
                Button(action: {
                    print("forgot pass")
                }) {
                    Text("забыли пароль?")
                        .fontWeight(.medium)
                        .foregroundColor(.white).opacity(0.5)
                        .font(.system(size: 12))
                }
            }
        }.padding(.horizontal, 20)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
