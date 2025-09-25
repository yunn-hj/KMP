//
// Created by openobject2 on 2025. 9. 8..
//

import SwiftUI
import Foundation
import Shared

struct TodoItem: View {
    let todoWithCategory: TodoWithCategory
    var onTodoChange: (Todo) -> Void
    var onTodoDelete: (Todo) -> Void
    var onCategoryClick: () -> Void

    @State private var contentInput: String
    @State private var isContentEditing: Bool = false
    @FocusState private var isFocused: Bool

    private let prevContentInput: String
    
    init(
        todoWithCategory: TodoWithCategory,
        onTodoChange: @escaping (Todo) -> Void,
        onTodoDelete: @escaping (Todo) -> Void,
        onCategoryClick: @escaping () -> Void
    ) {
        self.todoWithCategory = todoWithCategory
        self.onTodoChange = onTodoChange
        self.onTodoDelete = onTodoDelete
        self.onCategoryClick = onCategoryClick

        _contentInput = State(initialValue: todoWithCategory.todo.content)
        self.prevContentInput = todoWithCategory.todo.content
    }
    
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            CheckBox(
                isChecked: todoWithCategory.todo.isCompleted,
                onCheckedChange: { checked in
                    onTodoChange(
                        todoWithCategory
                            .todo
                            .doCopyCompleted(isCompleted: checked)
                            .toModel()
                    )
                }
            )
            VStack(alignment: .leading, spacing: 4) {
                Text(todoWithCategory.category.name)
                    .font(.caption)
                    .foregroundColor(getColor(colorName: todoWithCategory.category.color))
                    .onTapGesture(perform: onCategoryClick)
                
                HStack(alignment: .center) {
                    TextField(
                        "새로운 할일",
                        text: $contentInput,
                        onCommit: {
                            onTodoChange(
                                todoWithCategory
                                    .todo
                                    .doCopyContent(content: contentInput)
                                    .toModel()
                            )
                            isContentEditing = false
                        }
                    )
                    .focused($isFocused)
                    .onChange(of: isFocused) {
                        isContentEditing = isFocused
                        if !isFocused {
                            onTodoChange(
                                todoWithCategory
                                    .todo
                                    .doCopyContent(content: contentInput)
                                    .toModel()
                            )
                        }
                    }
                    Spacer()
                    
                    if isContentEditing {
                        Button(action: {
                            contentInput = prevContentInput
                            isFocused = false
                        }) {
                            Image(systemName: "xmark").foregroundColor(.black)
                        }

                        Button(action: {
                            onTodoChange(
                                todoWithCategory
                                    .todo
                                    .doCopyContent(content:contentInput)
                                    .toModel()
                            )
                            isFocused = false
                        }) {
                            Image(systemName: "checkmark").foregroundColor(.black)
                        }
                    }
                }
                TodoDueDate(
                    todo: todoWithCategory.todo,
                    onDateChange: { dueMs in
                        onTodoChange(
                            todoWithCategory.todo.doCopyDueMs(dueMs: dueMs).toModel()
                        )
                    }
                )
            }
        }
        .buttonStyle(.borderless)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.vertical, 8)
        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
            Button(role: .destructive, action: {
                onTodoDelete(todoWithCategory.todo.toModel())
            }) {
                Label("Delete", systemImage: "trash.fill").foregroundColor(.white)
            }
        }
        .onChange(of: todoWithCategory.todo.content) {
            if !isContentEditing {
                contentInput = todoWithCategory.todo.content
            }
        }
    }
}

struct TodoDueDate: View {
    let todo: TodoEntity
    let onDateChange: (Int64) -> Void
    
    @State private var showPicker = false
    @State private var pickerType: PickerType = .date
    @State private var selectedDate: Date
    
    enum PickerType {
        case date, time
    }
    
    init(todo: TodoEntity, onDateChange: @escaping (Int64) -> Void) {
        self.todo = todo
        self.onDateChange = onDateChange
        _selectedDate = State(initialValue: Date(timeIntervalSince1970: Double(todo.dueMs) / 1000.0))
    }
    
    var body: some View {
        let dueDate = IOSDueDate(dueMs: todo.dueMs)
        
        HStack(alignment: .center) {
            Text(dueDate.dueDate).onTapGesture {
                self.pickerType = .date
                self.showPicker = true
            }
            Text(dueDate.dueTime).onTapGesture {
                self.pickerType = .time
                self.showPicker = true
            }
        }
        .font(.caption)
        .foregroundColor(.gray)
        .sheet(isPresented: $showPicker) {
            DatePickerView(
                selection: $selectedDate,
                pickerType: pickerType,
                onDone: {
                    onDateChange(Int64(selectedDate.timeIntervalSince1970 * 1000) as Int64)
                    showPicker = false
                },
                onCancel: {
                    selectedDate = Date(timeIntervalSince1970: Double(todo.dueMs) / 1000.0)
                    showPicker = false
                }
            )
        }
    }
}

struct DatePickerView: View {
    @Binding var selection: Date
    let pickerType: TodoDueDate.PickerType
    let onDone: () -> Void
    let onCancel: () -> Void
    
    var body: some View {
        NavigationView {
            VStack {
                switch pickerType {
                case .date:
                    DatePicker(
                        "날짜 선택",
                        selection: $selection,
                        displayedComponents: .date
                    )
                    .environment(\.locale, Locale(identifier: "ko_KR"))
                    .datePickerStyle(.graphical)
                    .labelsHidden()
                case .time:
                    DatePicker(
                        "시간 선택",
                        selection: $selection,
                        displayedComponents: .hourAndMinute
                    )
                    .environment(\.locale, Locale(identifier: "ko_KR"))
                    .datePickerStyle(.wheel)
                    .labelsHidden()
                }
                Spacer()
            }
            .padding()
            .navigationTitle(pickerType == .date ? "날짜 변경" : "시간 변경")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("취소", action: onCancel)
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("완료", action: onDone)
                }
            }
        }
    }
}
