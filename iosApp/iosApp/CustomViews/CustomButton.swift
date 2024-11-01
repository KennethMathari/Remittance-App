//
//  CustomButton.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CustomButton: View {
    var title: String
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.custom("Avenir", size: 16))
                .fontWeight(.bold)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(8)
        }
        .padding(.horizontal)
    }
}

#Preview {
    CustomButton(title: "Click Me", action: {})
}
