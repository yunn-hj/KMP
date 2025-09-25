//
//  TodoScreen.swift
//  iosApp
//
//  Created by openobject2 on 9/15/25.
//

import SwiftUI
import UIKit

struct TodoScreen: View {
    @ObservedObject var viewModel: IosTodoViewModel
    @State var showContent = false
    @State private var showCategoryDialog = false

    init(viewModel: IosTodoViewModel) {
        self.viewModel = viewModel
        notifyTodo(todoList: viewModel.todoList)
    }
    
    var body: some View {
        VStack {
            SearchItem(
                onSearch: { keyword in viewModel.searchTodo(keyword:keyword) },
                onAdd: { viewModel.insertTodo() }
            )
            List($viewModel.todoList, id:\.todo.id) { item in
                TodoItem(
                    todoWithCategory: item.wrappedValue,
                    onTodoChange: { todo in viewModel.updateTodo(todo: todo) },
                    onTodoDelete: { todo in viewModel.deleteTodo(todo: todo) },
                    onCategoryClick: {
                        viewModel.selectTodo(todo: item.wrappedValue)
                        showCategoryDialog = true
                    }
                ).listRowSeparator(.hidden)
            }.listStyle(.plain)
        }
        .sheet(isPresented: $showCategoryDialog) {
            CategoryDialog(
                viewModel: viewModel,
                isPresented: $showCategoryDialog
            )
            .presentationDetents([.fraction(0.8)])
            .presentationCornerRadius(28)
        }
    }
}
