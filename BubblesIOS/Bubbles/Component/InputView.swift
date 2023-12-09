//
//  InputView.swift
//  Bubbles
//
//  Created by Петр Михеев on 09.12.2023.
//

import SwiftUI

struct InputView: View {
    @Binding var text: String
    let placeholder: String
    var isSecureField = false
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            if isSecureField {
                SecureField(placeholder, text: $text)
                    .font(.system(size: 12))
                    .padding(.vertical, 10)
                    .padding(.leading, 12)
                    .foregroundColor(.white)
                    .background(Color(UIColor(named: "AccentColor") ?? .clear))
                    .cornerRadius(12)
                
            } else {
                TextField(placeholder, text: $text)
                    .font(.system(size: 12))
                    .padding(.vertical, 10)
                    .padding(.leading, 12)
                    .foregroundColor(.white)
                    .background(Color(UIColor(named: "AccentColor") ?? .clear))
                    .cornerRadius(12)
                    
            }
        }
    }
}

#Preview {
    InputView(text: .constant(""), placeholder: "name@example.com")
}
