//
//  LaunchView.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared
import SwiftUI

struct LaunchView: View {
    @State private var authAccount: UserAccount? = nil
    @ObservedObject private var appUser = UserDefault.shared2  // Observe UserDefault singleton
    
    var body: some View {
        // Show LaunchView when not logged in
        NavigationStack {
            VStack {
                Spacer()
                
                // Logo
                Image("TawakalPayLogo")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 100)
                
                // Main Text
                Text("No more hassle")
                    .font(.custom("YourFontName", size: 24))
                    .fontWeight(.bold)
                    .padding(.top, 20)
                
                Text("Send to a mobile wallet, bank account or cash pick up point around the world")
                    .font(.custom("YourFontName", size: 14))
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                    .padding(.top, 10)
                
                Spacer()
                
                // Buttons
                CustomButton(title: "Sign In") {
                    print("Sign In tapped")
                    self.signInSignUp()
                }
                .frame(height: 48)
                
                CustomButton(title: "Create account") {
                    print("Create account tapped")
                    self.signInSignUp()
                }
                
                CustomButton(title: "Try it out") {
                    print("Try it out tapped")
                }
                
                // Terms & Contacts
                Button(action: {
                    print("Terms & Contacts tapped")
                }) {
                    Text("Terms & Contacts")
                        .font(.custom("YourFontName", size: 14))
                        .foregroundColor(.blue)
                        .padding(.top, 20)
                }
                
                Spacer()
            }
            .padding()
        }
        //        .onAppear{
        //            self.loadCurrentAccount()
        //        }
    }

    // Function for signing in
    func signInSignUp() {
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
                let result = try await authManager.signInSignUp()  // Call the signInSignUp function
                DispatchQueue.main.async {
                    
                    switch result {
                    case let success as AuthResult.Success:
                        authAccount = success.account  // Assuming success
                        print("result: \(String(describing: success.account?.mobile))")
                        appUser.isLoggedIn = true
                    case _ as AuthResult.Failure:
                        authAccount = nil
                    default:
                        print("Unexpected result")
                    }
                }
            } catch {
                authAccount = nil
                print("An error occurred: \(error.localizedDescription)")
            }
        }
    }
    func loadCurrentAccount() {
        Task {
            let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
            let result = try await authManager.loadCurrentAccount()  // Call the initialize function
            
            // Handle the result if necessary
            switch result {
                case let success as AuthResult.Success:
                    // Update your UI with the user account data
                authAccount = success.account
                appUser.isLoggedIn = true
                case let failure as AuthResult.Failure:
                    // Handle the failure case
                    print("Initialization failed with error: \(failure.error)")
                default:
                    // Handle any other cases
                    print("Unexpected result")
            }
        }
    }

}

struct LaunchView_Previews: PreviewProvider {
    static var previews: some View {
        LaunchView()
    }
}
