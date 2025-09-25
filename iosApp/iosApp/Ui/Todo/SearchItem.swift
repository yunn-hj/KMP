//
// Created by openobject2 on 2025. 9. 9..
//

import SwiftUI
import Foundation

struct SearchItem: View {
    @State var input = ""
    var onSearch: (String) -> Void
    var onAdd: () -> Void
    var body: some View {
        HStack {
            TextField("검색어를 입력하세요", text: $input)
            .onSubmit { onSearch(input) }
            Button(action: onAdd) {
                Image(systemName: "plus").tint(.black)
            }
        }
        .padding()
    }
}
