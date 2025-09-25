//
//  CategoryDialog.swift
//  iosApp
//
//  Created by openobject2 on 9/10/25.
//

import Shared
import SwiftUI

struct CategoryDialog: View {

    @ObservedObject var viewModel: IosTodoViewModel
    @Binding var isPresented: Bool
    @State private var selectedCategoryId: Int64
    @State private var input: String = ""

    init(viewModel: IosTodoViewModel, isPresented: Binding<Bool>) {
        self.viewModel = viewModel
        self._selectedCategoryId = State(initialValue: viewModel.selectedTodo?.category.id ?? -1)
        self._isPresented = isPresented
    }
    
    var body: some View {
        NavigationStack {
            VStack {
                List($viewModel.categoryList, id:\.id) { item in
                    CategoryDialogItem(
                        todo: viewModel.selectedTodo,
                        category: item.wrappedValue,
                        onTodoCategoryEdit: { todo in
                            if (todo != nil) {
                                viewModel.updateTodo(todo: todo!.todo.toModel())
                            }
                            isPresented = false
                        },
                        onCategoryDelete: { category in
                            viewModel.deleteCategory(category: category)
                        },
                        onCategoryEdit: { category in
                            viewModel.updateCategory(category: category)
                        }
                    ).listRowSeparator(.hidden)
                }.listStyle(.plain)
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Button(action: { viewModel.insertCategory() }) {
                        Image(systemName: "plus")
                    }
                }
            }
        }
    }
}
