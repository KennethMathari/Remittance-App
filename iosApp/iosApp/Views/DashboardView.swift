//
//  DashboardView.swift
//  iosApp
//
//  Created by Abdirahman on 10/14/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared
struct DashboardView: View {
    var userAccount: UserAccount?

    @EnvironmentObject var coordinator: Coordinator
    var body: some View {
        TabView {
            SendMoneyTabView()
                .tabItem {
                    Label("Home", systemImage: "house")
                }
            HistoryTabView()
                .tabItem {
                    Label("History", systemImage: "clock")
                }
            
            UserDetailView(userAccount: userAccount)
                .tabItem {
                    Label("Setting", systemImage: "gearshape")
                }
        }
    }
}

#Preview {
    DashboardView()
}
