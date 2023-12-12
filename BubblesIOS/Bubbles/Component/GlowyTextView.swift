//
//  GlowyTextView.swift
//  Bubbles
//
//  Created by Петр Михеев on 10.12.2023.
//

import SwiftUI

struct GlowyTextView: View {
    let text: String
    var colors: [Color]
    var fontSize: CGFloat
    
    init(text: String, colors: [Color], fontSize: CGFloat = 24) {
        self.text = text
        self.colors = colors
        self.fontSize = fontSize
    }
    
    var body: some View {
        ZStack {
            Text(text)
                .font(.system(size: fontSize))
                .fontWeight(.bold)
                .overlay(
                    LinearGradient(
                        gradient: Gradient(colors: colors),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                    .mask(Text(text)
                        .font(.system(size: fontSize))
                        .fontWeight(.bold)
                    )
                ).blur(radius: 32)
            Text(text)
                .font(.system(size: fontSize))
                .fontWeight(.bold)
                .overlay(
                    LinearGradient(
                        gradient: Gradient(colors: colors),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                    .mask(Text(text)
                        .font(.system(size: fontSize))
                        .fontWeight(.bold)
                    )
                )
        }
    }
}


#Preview {
    GlowyTextView(text: "Пример текста", colors: [.red, .blue, .green, .yellow], fontSize: 24)
    
}
