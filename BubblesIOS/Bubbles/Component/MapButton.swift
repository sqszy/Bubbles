//
//  MapButton.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct MapButton: View {
    var image: String = ""
    var body: some View {
        Image(systemName: image)
            .scaledToFill()
            .font(.system(size: 30))
            .frame(width: 60, height: 60)
            .foregroundColor(.white)
            .background(
                LinearGradient(
                    colors: [.black, .black.opacity(0.8)],
                    startPoint: .top,
                    endPoint: .bottom
                )
            )
            .cornerRadius(10)
    }
}

#Preview {
    MapButton(image: "magnifyingglass")
}
