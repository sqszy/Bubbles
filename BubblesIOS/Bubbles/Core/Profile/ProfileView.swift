//
//  ProfileView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI
import PhotosUI

struct ProfileView: View {
    @EnvironmentObject var authVM: AuthViewModel
    @ObservedObject var profileListVM = ProfileListViewModel()
    @State var selectedPhoto: [PhotosPickerItem] = []
    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            VStack(spacing: 40) {
                Text("АККАУНТ")
                    .font(.system(size: 24))
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                VStack(alignment: .leading, spacing: 40) {
                    if let user = profileListVM.user { // Проверяем, доступен ли пользователь
                        ProfileInfo(
                            title: "АВАТАРКА",
                            value: user.image != nil ?
                            AnyView(
                                ZStack {
                                    AsyncImage(url: URL(string: user.image!)!) { phase in
                                        switch phase {
                                        case .empty:
                                            ProgressView()
                                        case .success(let image):
                                            image
                                                .resizable()
                                                .aspectRatio(contentMode: .fill)
                                                .frame(width: 50, height: 50) // Установите размер изображения по вашему желанию
                                        case .failure:
                                            Image(systemName: "exclamationmark.triangle")
                                                .resizable()
                                                .frame(width: 50, height: 50) // Заглушка для случая ошибки загрузки изображения
                                        @unknown default:
                                            EmptyView()
                                        }
                                    }
                                    PhotosPicker(selection: $selectedPhoto, maxSelectionCount: 1, matching: .images) {
                                        Image(systemName: "camera.fill").foregroundColor(.white)
                                    }.onChange(of: selectedPhoto) { newValue in
                                        guard let item = newValue.first else {
                                            return
                                        }
                                        item.loadTransferable(type: Data.self) { result in
                                            switch result {
                                            case .success(let data):
                                                if let data = data {
                                                    profileListVM.uploadAvatarImage(imageData: data)
                                                } else {
                                                    print("Data is nil")
                                                }
                                            case .failure(let error):
                                                fatalError("\(error)")
                                            }
                                        }
                                    }
                                }
                            ) :
                                AnyView(
                                    PhotosPicker(selection: $selectedPhoto, maxSelectionCount: 1, matching: .images) {
                                        ZStack {
                                            Circle()
                                                .fill(Color.gray) // Цвет фона плейсхолдера
                                                .frame(width: 80, height: 80)
                                            
                                            Text(user.username.prefix(1)) // Отображаем только первую букву имени пользователя
                                                .font(.headline)
                                                .foregroundColor(.white)
                                        }
                                    }.onChange(of: selectedPhoto) { newValue in
                                        guard let item = newValue.first else {
                                            return
                                        }
                                        item.loadTransferable(type: Data.self) { result in
                                            switch result {
                                            case .success(let data):
                                                if let data = data {
                                                    profileListVM.uploadAvatarImage(imageData: data)
                                                } else {
                                                    print("Data is nil")
                                                }
                                            case .failure(let error):
                                                fatalError("\(error)")
                                            }
                                        }
                                    }
                                )
                        )
                        
                        ProfileInfo(title: "НИКНЕЙМ", value: AnyView(Text(user.username)))
                        ProfileInfo(title: "ЗАРЕГИСТРИРОВАН", value: AnyView(Text(formattedDate(user.registeredAt))))
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
    var value: AnyView // Изменяем тип value на AnyView
    
    var body: some View {
        HStack {
            Text(title)
            Spacer()
            value // Отображаем переданное значение
        }
    }
}

#Preview {
    ProfileView()
}
