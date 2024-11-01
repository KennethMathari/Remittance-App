//
//  Coordinator.swift
//  iosApp
//
//  Created by Abdirahman on 10/14/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
enum AppRoute: Hashable {
    case home
    case profile
    case history
    case imageview
    case detail(item: String)
}
// Enum for different root views
enum RootRoute {
    case login
    case mainApp
    case onboarding
}

class Coordinator: ObservableObject {
    // Tracks the current root view
    @Published var currentRoot: RootRoute = .login  // Default root
    @Published var navigationPath = NavigationPath()  // For internal navigation in current root

    // Change the root view based on the app's state
    func changeRoot(to route: RootRoute) {
        currentRoot = route
        navigationPath = NavigationPath()  // Reset navigation path when changing the root
    }

    // Navigate within the current root view
    func navigate(to route: AppRoute) {
        navigationPath.append(route)
    }

    // Go back to the root of the current stack
    func goToRoot() {
        navigationPath.removeLast(navigationPath.count)
    }
}
