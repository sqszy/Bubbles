//
//  ProfileView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var authVM: AuthViewModel
    @ObservedObject var profileListVM = ProfileListViewModel()
    
    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            VStack(spacing: 40) {
                Text("АККАУНТ")
                    .font(.system(size: 24))
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                VStack(alignment: .leading, spacing: 40) {
                    if let user = profileListVM.user { // Проверяем, доступен ли пользователь
                        ProfileInfo(title: "АВАТАРКА", value: user.image ?? "")
                        ProfileInfo(title: "НИКНЕЙМ", value: user.username)
                        ProfileInfo(title: "ЗАРЕГИСТРИРОВАН", value: formattedDate(user.registeredAt))
                    } else {
                        Text("Loading...")
                    }
//                    ProfileInfo(title: "АВАТАРКА", value: "image")
//                    ProfileInfo(title: "НИКНЕЙМ", value: "username")
//                    ProfileInfo(title: "ВОЗРАСТ", value: "22 года (01/01/2001)")
//                    ProfileInfo(title: "ДОБАВЛЕННЫЕ МЕСТА", value: "22")
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
        .onAppear {
            profileListVM.getUser()
        }
    }
}

private func formattedDate(_ dateString: String) -> String {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ" // Формат входной даты
    
    if let date = formatter.date(from: dateString) {
        formatter.dateFormat = "dd MMMM yyyy" // Формат выходной даты (пример: "10 декабря 2023")
        return formatter.string(from: date)
    } else {
        return "Дата неизвестна"
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
