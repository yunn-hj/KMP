//
//  CategoryColorPalete.swift
//  iosApp
//
//  Created by openobject2 on 9/11/25.
//

import SwiftUI
import UIKit

struct CategoryColorPalette: View {
    @State var selectedColor: String
    let onColorSelected: (String) -> Void
    
    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            Color.red
                .onTapGesture {
                    selectedColor = "red"
                    onColorSelected(selectedColor)
                }
                .border(selectedColor == "red" ? Color.black : Color.clear, width: 2)
            
            Color.yellow
                .onTapGesture {
                    selectedColor = "yellow"
                    onColorSelected(selectedColor)
                }
                .border(selectedColor == "yellow" ? Color.black : Color.clear, width: 2)
            
            Color.green
                .onTapGesture {
                    selectedColor = "green"
                    onColorSelected(selectedColor)
                }
                .border(selectedColor == "green" ? Color.black : Color.clear, width: 2)
            
            Color.blue
                .onTapGesture {
                    selectedColor = "blue"
                    onColorSelected(selectedColor)
                }
                .border(selectedColor == "blue" ? Color.black : Color.clear, width: 2)
            
            Color.gray
                .onTapGesture {
                    selectedColor = "gray"
                    onColorSelected(selectedColor)
                }
                .border(selectedColor == "gray" ? Color.black : Color.clear, width: 2)
        }
    }
}
