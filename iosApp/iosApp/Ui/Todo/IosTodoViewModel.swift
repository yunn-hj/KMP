//
// Created by openobject2 on 2025. 9. 9..
//

import Foundation
import Shared
import Combine
import Dispatch
import KMPNativeCoroutinesCombine

class IosTodoViewModel: ObservableObject {
    private let todoRepo = Repositories.shared.todoRepository
    private let categoryRepo = Repositories.shared.categoryRepository
    private let todoViewModel = ViewModels.shared.todoViewModel

    @Published var selectedTodo: TodoWithCategory? = nil
    @Published var todoList: [TodoWithCategory] = []
    @Published var categoryList: [CategoryModel] = []
    
    private var cancellables = Set<AnyCancellable>()

    init() {
        createPublisher(for: todoViewModel.state)
        .receive(on: DispatchQueue.main)
        .sink { completion in
            if case .failure(let error) = completion {
                dump("yhj_todoViewModel >> stateFlow subscription failed: \(error)")
            }
        } receiveValue: { [weak self] todoState in
            // KMP의 todoState가 넘어오면 @Published 프로퍼티 업데이트
            self?.todoList = todoState.todoList
            self?.categoryList = todoState.categoryList
            self?.selectedTodo = todoState.selectedTodo
            dump("yhj_todoViewModel >> new state received, todoList count: \(todoState.todoList.count)")
        }
        .store(in: &cancellables) // 구독을 저장해서 메모리에서 해제되지 않도록 관리

        Task {
            do {
                let defaultCategory = (CategoryModelImplKt.getDefaultCategory() as! CategoryModelImpl).doCopyId(id: 1).toEntity()
                try await categoryRepo.insert(category: defaultCategory)
                
                todoViewModel.loadTodo()
                todoViewModel.loadCategory()
            } catch {
                dump("yhj_todoViewModel >> viewmodel init failed")
            }
        }
    }

    func loadTodo() {
        todoViewModel.loadTodo()
    }

    func loadCategory() {
        todoViewModel.loadCategory()
    }
    
    func searchTodo(keyword: String) {
        todoViewModel.searchTodo(keyword: keyword)
    }

    func insertTodo(todo: Todo = TodoImplKt.getDefaultTodo()) {
        todoViewModel.insertTodo(todo: todo)
    }

    func insertCategory(category: CategoryModel = CategoryModelImplKt.getDefaultCategory()) {
        todoViewModel.insertCategory(category: category)
    }

    func updateTodo(todo: Todo) {
        todoViewModel.updateTodo(todo: todo)
    }

    func updateCategory(category: CategoryModel) {
        todoViewModel.updateCategory(category: category)
    }

    func deleteTodo(todo: Todo){
        todoViewModel.deleteTodo(todo: todo)
    }

    func deleteCategory(category: CategoryModel){
        todoViewModel.deleteCategory(category: category)
    }

    func selectTodo(todo: TodoWithCategory? = nil){
        todoViewModel.selectTodo(todo: todo)
    }
}
