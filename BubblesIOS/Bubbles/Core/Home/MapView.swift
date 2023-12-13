//
//  MapView.swift
//  Bubbles
//
//  Created by Петр Михеев on 13.12.2023.
//

import SwiftUI
import MapKit

struct PlaceDetails: View {
    @State private var isHeartRed = false
    @State private var isHeartScaled = false
    @ObservedObject var placeVM = PlaceViewModel()
    let placeID: String
    
    init(placeID: String) {
        self.placeID = placeID
        self.placeVM = PlaceViewModel()
        self.placeVM.getPlace(id: placeID)
    }
    
    
    var body: some View {
        if let place = placeVM.place {
            ZStack {
                Color.black.ignoresSafeArea(.all)
                VStack(spacing: 20) {
                    VStack(alignment: .leading, spacing: 10) {
                        GlowyTextView(text: place.title, colors: [Color.pBlue, Color.pBlueTo], fontSize: 24)
                        Text(place.about)
                            .font(.system(size: 14))
                            .foregroundStyle(Color.onTop)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    ScrollView(.horizontal) {
                        HStack(spacing: 20) {
                            if let images = placeVM.place?.images {
                                ForEach(images, id: \.self) { imageName in
                                    AsyncImage(
                                        url: URL(string: imageName),
                                        transaction: Transaction(animation: .easeInOut)
                                    ) { phase in
                                        switch phase {
                                        case .empty:
                                            ProgressView()
                                        case .success(let image):
                                            image
                                                .resizable()
                                                .transition(.scale(scale: 0.85, anchor: .center))
                                        case .failure:
                                            Image(systemName: "wifi.slash")
                                        @unknown default:
                                            EmptyView()
                                        }
                                    }
                                    .frame(width: 260, height: 240)
                                    .background(Color.gray)
                                    .cornerRadius(12)
                                }
                            }
                        }
                    }

                    HStack {
                        PlaceButton(colors: [Color.pBlue, Color.pBlueTo], text: "Построить маршрут")
                        Spacer()
                        PlaceButton(text: "Пожаловаться").foregroundStyle(.white)
                        Spacer()
                        Button {
                            // При нажатии на кнопку
                            withAnimation {
                                isHeartScaled = true
                                isHeartRed = true
                            }

                            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                                withAnimation {
                                    isHeartScaled = false
                                }
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    withAnimation {
                                        isHeartRed = false
                                    }
                                }
                            }
                        } label: {
                            Image(systemName: "heart.fill")
                                .foregroundColor(isHeartRed ? .red : .white)
                                .frame(width: 24, height: 24)
                                .scaleEffect(isHeartScaled ? 1.5 : 1.0)
                        }
                        
                    }
                    
                    HStack {
                        Text("Отзывы")
                            .font(.system(size: 14, weight: .medium))
                    }
                    
                    ScrollView(.vertical) {
                        VStack(spacing: 20) {
                            if let reviews = placeVM.place?.reviews {
                                ForEach(reviews, id: \.self) { review in
                                    HStack {
                                        AsyncImage(
                                            url: URL(string: ""),
                                            transaction: Transaction(animation: .easeInOut)
                                        ) { phase in
                                            switch phase {
                                            case .empty:
                                                ProgressView()
                                            case .success(let image):
                                                image
                                                    .resizable()
                                                    .transition(.scale(scale: 0.85, anchor: .center))
                                            case .failure:
                                                Image(systemName: "wifi.slash")
                                            @unknown default:
                                                EmptyView()
                                            }
                                        }
                                        .frame(width: 32, height: 32)
                                        .background(Color.gray)
                                        .cornerRadius(12)
                                        
                                        VStack {
                                            HStack {
                                                Text(review.creatorId)
                                                    .font(.system(size: 10, weight: .medium))
                                                Text("10 дней назад")
                                                    .font(.system(size: 10, weight: .medium))
                                            }
                                            Text(review.text)
                                                .font(.system(size: 12, weight: .medium))
                                                .foregroundStyle(.white)
                                        }
                                    }
                                }
                            } else {
                                ProgressView()
                            }
                        }
                    }
                    
                    Spacer()
                }
                .padding(.top, 40)
                .padding(.horizontal, 20)
            }
            .onAppear {
                placeVM.getPlace(id: placeID)
            }
        } else {
            ProgressView()
        }
    }
}

struct MapView: View {
    @StateObject var viewModel = HomeMapViewModel()
    @State private var position: MapCameraPosition = .userLocation(fallback: .automatic)
    @State private var showDetails: Bool = false
    @State private var selectedPlaceID: String? = nil

    var body: some View {
        Map(position: $position) {
            UserAnnotation()
            ForEach(viewModel.places ?? [], id: \.id) { place in
                Annotation(place.title, coordinate: CLLocationCoordinate2D(latitude: place.latitude, longitude: place.longitude)) {
                    AsyncImage(
                        url: URL(string: place.images.first ?? ""),
                        transaction: Transaction(animation: .easeInOut)
                    ) { phase in
                        switch phase {
                        case .empty:
                            ProgressView()
                        case .success(let image):
                            Button {
                                showDetails.toggle()
                                selectedPlaceID = place._id
                            } label : {
                                image
                                    .resizable()
                                    .transition(.scale(scale: 0.1, anchor: .center))
                                    .sheet(isPresented: $showDetails) {
                                        if let placeID = selectedPlaceID {
                                            PlaceDetails(placeID: placeID)
                                        }
                                    }
                            }
                        case .failure:
                            Image(systemName: "wifi.slash")
                        @unknown default:
                            EmptyView()
                        }
                    }
                    .frame(width: 52, height: 52)
                    .background(Color.gray)
                    .clipShape(Circle())
                }
            }
        }
        .onAppear {
            viewModel.getPlaces()
        }
        .onAppear {
            CLLocationManager().requestWhenInUseAuthorization()
        }
    }
}

#Preview {
    MapView()
}
