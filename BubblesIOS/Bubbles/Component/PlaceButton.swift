//
//  PlaceButton.swift
//  Bubbles
//
//  Created by Петр Михеев on 13.12.2023.
//

import SwiftUI

struct PlaceButton: View {
    var colors: [Color]?
    var text: String

    init(colors: [Color]? = nil, text: String) {
        self.colors = colors
        self.text = text
    }

    var body: some View {
        let defaultGradientColors: [Color] = [Color.secondary] // Цвет по умолчанию, например, серый

        let gradient = colors.map {
            LinearGradient(
                gradient: Gradient(colors: $0),
                startPoint: .leading,
                endPoint: .trailing
            )
        } ?? LinearGradient(
            gradient: Gradient(colors: defaultGradientColors),
            startPoint: .leading,
            endPoint: .trailing
        )

        return Button {
            // Действия кнопки
        } label: {
            Text(text)
                .font(.system(size: 12, weight: .medium))
                .padding(.vertical, 10)
                .padding(.horizontal, 20)
                .foregroundStyle(.black)
        }
        .background(gradient)
        .cornerRadius(12)
    }
}

#Preview {
    PlaceButton(text: "Построить маршрут")
}
