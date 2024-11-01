import SwiftUI
import Shared
struct ContentView: View {
    let authManager = IosAuthenticationManager()
    @State private var showContent = false
    @State private var authAccount: UserAccount? = nil
    @State private var authError: String? = nil
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    //                    showContent = !showContent
                    authenticateUser()
                    
                }
            }
            Button("Acquire Token Silently") {
                acquireTokenSilently()
            }
            Button("Sign Up") {
                signInSignUp()
            }
            Button("Sign Out") {
                signOut()
            }


            
            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    //                    Text("SwiftUI: \(Greeting().greet())")
                    if let account = authAccount {
                        Text("Account Name: \(account.givenName)")  // Display the username from the UserAccount model
                            .font(.headline)
                            .foregroundColor(.green)

                        Text("Account ID: \(account.accountId)")  // Display the account ID from the UserAccount model
                            .font(.subheadline)
                            .foregroundColor(.blue)
                    } else if let error = authError {
                        Text("Authentication Error: \(error)")
                            .font(.headline)
                            .foregroundColor(.red)
                    }
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .onAppear {
            self.initializeMSAL()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
    // Call the Kotlin MSAL authentication method
    func authenticateUser() {
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Instance of the Kotlin AuthManagerIOS class
                let result = try await authManager.acquireToken()  // Call the Kotlin suspend function with 'try'
                
                DispatchQueue.main.async {
                    switch result {
                    case let success as AuthResult.Success:
                        authAccount = success.account  // Now dealing with UserAccount model
                        authError = nil
                        showContent = true
                    case let failure as AuthResult.Failure:
                        authError = failure.error
                        authAccount = nil
                        showContent = true
                    default:
                        authError = "Unknown error"
                        authAccount = nil
                        showContent = true
                    }
                }
            } catch {
                // Handle any unexpected errors here
                DispatchQueue.main.async {
                    authError = error.localizedDescription
                    authAccount = nil
                    showContent = true
                }
            }
        }
    }
    func acquireTokenSilently() {
        // Make the function async using Task to interact with the Kotlin suspend function
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Instance of the Kotlin class
                let result = try await authManager.acquireTokenSilent()  // Call the Kotlin suspend function

                // Handle the result in Swift
                DispatchQueue.main.async {
                    switch result {
                    case let success as AuthResult.Success:
                        authAccount = success.account
                        authError = nil
                        showContent = true
                    case let failure as AuthResult.Failure:
                        authError = failure.error
                        authAccount = nil
                        showContent = true
                    default:
                        authError = "Unknown error"
                        authAccount = nil
                        showContent = true
                    }
                }
            } catch {
                // Handle any unexpected errors
                DispatchQueue.main.async {
                    authError = "An error occurred: \(error.localizedDescription)"
                    authAccount = nil
                    showContent = true
                }
            }
        }
    }
    func initializeMSAL() {
        Task {
            let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
            let result = try await authManager.loadCurrentAccount()  // Call the initialize function
            
            // Handle the result if necessary
            switch result {
                case let success as AuthResult.Success:
                    // Update your UI with the user account data
                authAccount = success.account
                authError = nil
                showContent = true
                case let failure as AuthResult.Failure:
                    // Handle the failure case
                    print("Initialization failed with error: \(failure.error)")
                default:
                    // Handle any other cases
                    print("Unexpected result")
            }
        }
    }
    func signInSignUp() {
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
                let result = try await authManager.signInSignUp()  // Call the signInSignUp function
                DispatchQueue.main.async {
                    
                    switch result {
                    case let success as AuthResult.Success:
                        authAccount = success.account  // Assuming success returns UserAccount
                        authError = nil
                        showContent = true
                        print("result: \(String(describing: success.account?.mobile))")
                    case let failure as AuthResult.Failure:
                        authError = failure.error
                        authAccount = nil
                        showContent = false
                    default:
                        print("Unexpected result")
                    }
                }
            } catch {
                authError = "An error occurred: \(error.localizedDescription)"
                authAccount = nil
                showContent = false
                print("An error occurred: \(error.localizedDescription)")
            }
        }
    }
    func signOut() {
        Task {
            do {
                let authManager = IosAuthenticationManager()  // Create an instance of your Kotlin AuthManager
                let result = try await authManager.signOutB2C()  // Call the signOutB2C function
                
                switch result {
                case let success as AuthResult.Success:
                    print("Sign out successful")
                    // Handle successful sign out (e.g., update UI)
                    if let account = success.account {
                        // Do something with the account if it's not null
                    } else {
                        print("No account was found after sign out.")
                    }
                case let failure as AuthResult.Failure:
                    print("Sign out failed: \(failure.error)")
                    // Handle failure (e.g., show error message)
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

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
