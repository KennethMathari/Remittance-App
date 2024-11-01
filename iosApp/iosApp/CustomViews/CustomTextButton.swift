//
//  CustomTextButton.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CustomTextButton: View {
    var title: String
    var action: () -> Void
    var fontColor: Color = .white
    var backgroundColor: Color = .blue
    var fontSize: CGFloat = 18
    var cornerRadius: CGFloat = 8
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: fontSize))
                .foregroundColor(fontColor)
                .padding()
                .frame(maxWidth: .infinity)
                .background(backgroundColor)
                .cornerRadius(cornerRadius)
        }
        .padding(.horizontal)
    }
}

#Preview {
    CustomTextButton(title: "button", action: {})
}
