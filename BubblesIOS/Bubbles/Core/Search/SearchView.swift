//
//  SearchView.swift
//  Bubbles
//
//  Created by Петр Михеев on 12.12.2023.
//

import SwiftUI


// ViewModel для поиска мест
//class SearchViewMod: ObservableObject {
//    @Published var result: [Places]?
//
//    // Функция для симуляции получения данных
//    func fetchPlaces() {
//        // Симулируем получение данных о местах
//        let placesData = [
//            Places(title: "Дом", about: "Тепло и уютно"),
//            Places(title: "Дом", about: "Крутой собор")
//        ]
//        // Устанавливаем полученные данные в result
//        self.result = placesData
//    }
//}

struct SearchView: View {
    @ObservedObject var searchVM = SearchViewModel()
    @State private var searchText: String = "" // Добавляем состояние для хранения текста из TextField
    
    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            VStack {
                HStack(spacing: 16) {
                    Image(systemName: "magnifyingglass").foregroundColor(.white)
                        .padding(.leading, 8)
                    TextField("", text: $searchText, prompt: Text("Поиск").foregroundStyle(.white))
                        .font(.system(size: 14))
                        .foregroundStyle(.white)
                }
                .padding(16)
                .background(Color(UIColor(named: "AccentColor") ?? .clear))
                .cornerRadius(12)
                
                Button {
                    searchVM.search(query: searchText)
                } label: {
                    Text("search").foregroundStyle(.white)
                }
                
                if let places = searchVM.places {
                    ScrollView {
                        LazyVStack(spacing: 20) {
                            ForEach(places) { place in
                                HStack(spacing: 20) {
                                    AsyncImage(
                                        url: URL(string: place.images.first ?? ""),
                                        transaction: Transaction(animation: .easeInOut)
                                    ) { phase in
                                        switch phase {
                                        case .empty:
                                            ProgressView()
                                        case .success(let image):
                                            image
                                                .resizable()
                                                .transition(.scale(scale: 0.1, anchor: .center))
                                        case .failure:
                                            Image(systemName: "wifi.slash")
                                        @unknown default:
                                            EmptyView()
                                        }
                                    }
                                    .frame(width: 60, height: 60)
                                    .background(Color.gray)
                                    .clipShape(Circle())
                                    VStack (alignment: .leading) {
                                        Text(place.title)
                                            .font(.system(size: 14, weight: .bold))
                                            .foregroundColor(.white)
                                        Text(place.about)
                                            .font(.system(size: 10))
                                            .foregroundStyle(Color.onTop)
                                    }
                                    Spacer()
                                }
                                .padding()
                                .background(Color(UIColor(named: "AccentColor") ?? .clear)) // Фон для каждого элемента
                                .cornerRadius(10)
                                .frame(maxWidth: .infinity)
                            }
                        }
                    }
                } else {
                    Text("Ищите!")
                        .foregroundColor(.white)
                    Spacer()
                }
            }.padding(.top, 50).padding(.horizontal, 20)
        }
    }
}




#Preview {
    SearchView()
}
