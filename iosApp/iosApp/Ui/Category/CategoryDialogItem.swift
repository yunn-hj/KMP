//
//  CategoryDialogItem.swift
//  iosApp
//
//  Created by openobject2 on 9/10/25.
//

import SwiftUI
import Foundation
import Shared

struct CategoryDialogItem: View {
    var todo: TodoWithCategory? = nil
    let category: CategoryModel
    var onTodoCategoryEdit: (TodoWithCategory?) -> Void
    var onCategoryEdit: (CategoryModel) -> Void
    var onCategoryDelete:(CategoryModel) -> Void
    
    @State private var nameInput: String
    @State private var isNameEditing: Bool = false
    @FocusState private var isFocused: Bool
    @State private var categoryColor: String = "gray"
    
    private let prevNameInput: String
    
    init(
        todo: TodoWithCategory? = nil,
        category: CategoryModel,
        onTodoCategoryEdit: @escaping (TodoWithCategory?) -> Void,
        onCategoryDelete: @escaping (CategoryModel) -> Void,
        onCategoryEdit: @escaping (CategoryModel) -> Void
    ) {
        self.todo = todo
        self.category = category
        self.categoryColor = category.color
        self.onTodoCategoryEdit = onTodoCategoryEdit
        self.onCategoryDelete = onCategoryDelete
        self.onCategoryEdit = onCategoryEdit
        
        _nameInput = State(initialValue: category.name)
        self.prevNameInput = category.name
    }
    
    var body: some View {
        VStack(alignment: .center, spacing: 6) {
            HStack(alignment: .center, spacing: 12) {
                TextField(
                    "새로운 카테고리",
                    text: $nameInput,
                    onCommit: {
                        onCategoryEdit(
                            (category as! CategoryModelImpl).doCopyName(name: nameInput)
                        )
                        isNameEditing = false
                    }
                )
                .focused($isFocused)
                .onChange(of: isFocused) {
                    isNameEditing = isFocused
                    if !isFocused {
                        let updatedCategory = (category as! CategoryModelImpl).doCopyName(name: nameInput)
                        
                        onCategoryEdit(updatedCategory)
                        if category.id == todo?.category.id {
                            onTodoCategoryEdit(todo?.doCopy(todo: todo!.todo, category: updatedCategory.toEntity()))
                        }
                    }
                }
                .foregroundColor(getColor(colorName: categoryColor))
            
                Spacer()
                
                if isNameEditing {
                    Button(action: {
                        nameInput = prevNameInput
                        isFocused = false
                    }) {
                        Image(systemName: "xmark")
                    }
                    
                    Button(action: {
                        isFocused = false
                    }) {
                        Image(systemName: "arrow.right")
                    }
                }
                else {
                    Button(action: {
                        if todo != nil {
                            onTodoCategoryEdit(
                                todo!.doCopy(
                                    todo: todo!.todo.doCopyCategoryId(categoryId: category.id),
                                    category: (category as! CategoryModelImpl).toEntity()
                                )
                            )
                        }
                    }) {
                        Text("선택")
                    }
                }
            }
            .buttonStyle(.borderless)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.vertical, 8)
            .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                Button(role: .destructive, action: {
                    onCategoryDelete(category)
                }) {
                    Label("Delete", systemImage: "trash.fill")
                }
            }
            .onChange(of: category.name) {
                if !isNameEditing {
                    nameInput = category.name
                }
            }
        }
        if (isNameEditing) {
            CategoryColorPalette(
                selectedColor: categoryColor,
                onColorSelected: { color in
                    categoryColor = color
                    onCategoryEdit((category as! CategoryModelImpl).doCopyColor(color: categoryColor))
                }
            )
        }
    }
}
