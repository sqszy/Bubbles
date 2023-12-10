//
//  MapAccountButton.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct MapAccountButton: View {
    var image: String = ""
    var body: some View {
        Image(systemName: image)
            .scaledToFill()
            .font(.system(size: 24))
            .frame(width: 40, height: 40)
            .foregroundColor(.white)
            .background(
                LinearGradient(
                    colors: [.black, .black.opacity(0.8)],
                    startPoint: .top,
                    endPoint: .bottom
                )
            )
            .cornerRadius(999)
    }
}

#Preview {
    MapAccountButton(image: "")
}
