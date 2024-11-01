//
//  ImagePickerView.swift
//  iosApp
//
//  Created by Abdirahman on 10/14/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import PhotosUI
import UIKit

struct ImagePickerView: View {
    @State private var showingImagePicker = false
    @State private var image: UIImage?
    @State private var sourceType: UIImagePickerController.SourceType = .photoLibrary
    @State private var showCropView = false
    
    var body: some View {
        VStack {
            if let image = image {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 300)
                    .cornerRadius(10)
                    .padding()
            } else {
                Image(systemName: "photo")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 300)
                    .padding()
            }
            
            HStack {
                Button(action: {
                    sourceType = .camera
                    showingImagePicker = true
                }) {
                    Text("Take Photo")
                }
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)
                
                Button(action: {
                    sourceType = .photoLibrary
                    showingImagePicker = true
                }) {
                    Text("Choose from Library")
                }
                .padding()
                .background(Color.green)
                .foregroundColor(.white)
                .cornerRadius(10)
            }
            .padding()
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(sourceType: sourceType, selectedImage: $image, showCropView: $showCropView)
        }
//        .sheet(isPresented: $showCropView) {
//            if let imageToCrop = image {
//                ImageCropView(image: imageToCrop, croppedImage: $image)
//            }
//        }
    }
}

#Preview {
    ImagePickerView()
}
struct ImagePicker: UIViewControllerRepresentable {
    var sourceType: UIImagePickerController.SourceType
    @Binding var selectedImage: UIImage?
    @Binding var showCropView: Bool

    func makeUIViewController(context: UIViewControllerRepresentableContext<ImagePicker>) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.sourceType = sourceType
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: UIViewControllerRepresentableContext<ImagePicker>) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        var parent: ImagePicker

        init(_ parent: ImagePicker) {
            self.parent = parent
        }

        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
            if let image = info[.originalImage] as? UIImage {
                parent.selectedImage = image
                parent.showCropView = true
            }
            picker.dismiss(animated: true)
        }
    }
}

