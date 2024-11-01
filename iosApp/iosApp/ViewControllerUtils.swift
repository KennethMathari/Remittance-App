//
//  ViewControllerUtils.swift
//  iosApp
//
//  Created by Abdirahman on 10/7/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit

@objc public class ViewControllerUtils: NSObject {
    
    @objc public static func getRootViewController() -> UIViewController? {
        if #available(iOS 13.0, *) {
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                return scene.windows.first?.rootViewController
            }
        } else {
            return UIApplication.shared.keyWindow?.rootViewController
        }
        return nil
    }
}
