//
//  ProfileView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var authVM: AuthViewModel
    
    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            VStack(spacing: 40) {
                Text("АККАУНТ")
                    .font(.system(size: 24))
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                VStack(alignment: .leading, spacing: 40) {
                    ProfileInfo(title: "АВАТАРКА", value: "image")
                    ProfileInfo(title: "НИКНЕЙМ", value: "username")
                    ProfileInfo(title: "ВОЗРАСТ", value: "22 года (01/01/2001)")
                    ProfileInfo(title: "ДОБАВЛЕННЫЕ МЕСТА", value: "22")
                }
                
                VStack(alignment: .leading, spacing: 40) {
                    HStack {
                        Spacer()
                    }
                    Button {
                        // Нажатие на кнопку "НАСТРОЙКИ"
                    } label: {
                        Text("НАСТРОЙКИ")
                    }
                    Button {
                        authVM.signOut()
                    } label: {
                        Text("ВЫЙТИ")
                    }
                }
                Spacer()
            }.padding(.top, 50).padding(.horizontal, 20)
        }
        .foregroundColor(.white)
    }
}

struct ProfileInfo: View {
    var title: String
    var value: String
    
    var body: some View {
        HStack {
            Text(title)
            Spacer() // Создаем пространство между заголовком и значением
            Text(value)
        }
    }
}

#Preview {
    ProfileView()
}
