//
//  UserDefault.swift
//  iosApp
//
//  Created by Abdirahman on 10/13/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
class UserDefault: NSObject, ObservableObject {
    static let shared2: UserDefault = UserDefault()
    @Published var isLoggedIn: Bool = false
}
