//
//  HomeView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct HomeView: View {
    @State var showingBottomSheet = false
    @State var showingSearchSheet = false
    var body: some View {
        ZStack {
            MapView()
                .ignoresSafeArea()
            
            VStack {
                HStack {
                    Spacer() // Занимает пространство слева, чтобы выровнять кнопки справа
                    VStack {
                        Button {
                            showingBottomSheet.toggle()
                        } label: {
                            MapAccountButton(image: "person.fill")
                        }.sheet(isPresented: $showingBottomSheet) {
                            ProfileView()
                            .presentationDetents([.large])
                            .presentationDragIndicator(.visible)
                        }
                        
                        Button {
                            // Действие для кнопки "Настройки"
                        } label: {
                            MapAccountButton(image: "gearshape.fill")
                        }
                    }
                }
                .padding(.horizontal, 16) // Добавляем отступы справа и слева для кнопок
                .padding(.top, 12) // Добавляем отступ сверху
                
                Spacer() // Занимает пространство под кнопками "Аккаунт" и "Настройки"
                
                HStack(spacing: 40) {
                    Button {
                        showingSearchSheet.toggle()
                    } label: {
                        MapButton(image: "magnifyingglass")
                    }.sheet(isPresented: $showingSearchSheet) {
                        SearchView()
                    }
                    
                    Button {
                        // Действие для второй кнопки
                    } label: {
                        MapButton(image: "mappin.and.ellipse")
                    }
                    
                    Button {
                        // Действие для третьей кнопки
                    } label: {
                        MapButton(image: "heart.fill")
                    }
                }
                .padding(.bottom, 12)
            }
        }
    }
}


#Preview {
    HomeView()
}
