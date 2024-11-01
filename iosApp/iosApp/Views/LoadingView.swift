//
//  LoadingView.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared
struct LoadingView: View {
    @State private var authAccount: UserAccount? = nil
    @ObservedObject private var appUser = UserDefault.shared2
    var body: some View {
        Group {
            if appUser.isLoggedIn, let account = authAccount {
                // If user is logged in, show UserDetailView
//                UserDetailView(userAccount: account)
                DashboardView(userAccount: account)
            } else {
                // Otherwise, show the LaunchView for login or registration
                LaunchView()
            }
        }
        .onAppear {
            loadCurrentAccount()  // Call the function when the view appears
        }
    }
    // Function to load the current account on app startup
    func loadCurrentAccount() {
        Task {
            let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
            let result = try await authManager.loadCurrentAccount()  // Call the initialize function
            
            DispatchQueue.main.async {
                // Handle the result
                switch result {
                case let success as AuthResult.Success:
                    // Update user account and set login state
                    authAccount = success.account
                    appUser.isLoggedIn = true  // Set login state to true
                case let failure as AuthResult.Failure:
                    // Handle the failure (e.g., no logged-in user found)
                    authAccount = nil
                    appUser.isLoggedIn = false
                    print("Initialization failed with error: \(failure.error)")
                default:
                    // Handle any other cases
                    print("Unexpected result")
                }
            }
        }
    }
}

#Preview {
    LoadingView()
}
