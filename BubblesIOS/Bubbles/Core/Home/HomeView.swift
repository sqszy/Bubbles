//
//  HomeView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct HomeView: View {
    var body: some View {
        HomeMapViewRepresentable()
            .ignoresSafeArea()
    }
}

#Preview {
    HomeView()
}
