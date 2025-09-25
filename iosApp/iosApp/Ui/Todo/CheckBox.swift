//
// Created by openobject2 on 2025. 9. 8..
//

import Foundation
import UIKit
import SwiftUI

struct CheckBox: View {
    var isChecked: Bool
    var onCheckedChange: (Bool) -> Void
    var body: some View {
        Button(action: { onCheckedChange(!isChecked) }) {
            Image(systemName: isChecked ? "checkmark.square.fill" : "square")
                .foregroundColor(isChecked ? Color(red: 115/255, green: 158/255, blue: 201/255) : .secondary)
        }.buttonStyle(.plain)
    }
}
