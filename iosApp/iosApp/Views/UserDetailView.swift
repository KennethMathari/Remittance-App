//
//  UserDetailView.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared
struct UserDetailView: View {
    var userAccount: UserAccount?
    @ObservedObject private var appUser = UserDefault.shared2
    @StateObject var coordinator = Coordinator()

    var body: some View {
        NavigationStack(path: $coordinator.navigationPath){
            VStack {
                Text("Welcome, \(userAccount?.mobile ?? "" )")
                    .font(.largeTitle)
                    .padding()
                
                Text("Account details:")
                    .font(.headline)
                    .padding(.top, 20)
                
                Text("Mobile: \(userAccount?.mobile ?? "")")
                    .padding(.top, 10)
                
                Text("Email: \(userAccount?.accountId ?? "")")
                    .padding(.top, 10)
                
                // Add more user details here
                Spacer()
                CustomTextButton(title: "Sign Out", action: signOut, fontColor: .white, backgroundColor: .red, fontSize: 18, cornerRadius: 8)
                
                CustomTextButton(title: "Upload document", action: { coordinator.navigate(to: .imageview) }, fontColor: .white, backgroundColor: .red, fontSize: 18, cornerRadius: 8)
                Spacer()
                
            }
            .padding()
            .navigationTitle("User Details")
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case .imageview:
                    ImagePickerView()
                case .home:
                    EmptyView()
                case .profile:
                    EmptyView()
                case .history:
                    EmptyView()
                case .detail(item: let item):
                    EmptyView()
                }
            }
        }
    }
    func signOut() {
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
                let result = try await authManager.signOutB2C()  // Call the signOutB2C function
                
                switch result {
                case _ as AuthResult.Success:
                    print("Sign out successful")
                    appUser.isLoggedIn = false
                case let failure as AuthResult.Failure:
                    print("Sign out failed: \(failure.error)")
                default:
                    print("Unexpected result")
                }
            } catch {
                print("Error during sign out: \(error.localizedDescription)")
                // Handle unexpected errors
            }
        }
    }

}

#Preview {
    UserDetailView(userAccount: UserAccount(
        username: "", accountId: "Email@gmail.com", mobile: "12345", city: "Boorama", name: "Abdi Abi", givenName: "Abdi Abile", familyName: "Abdi Abdi", streetAddress: "2222", referralVal: true, approve: true
    ))
}
