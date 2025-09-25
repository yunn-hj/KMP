package com.dosystem.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dosystem.todo.data.model.category.CategoryModel
import com.dosystem.todo.data.model.category.CategoryModelImpl
import com.dosystem.todo.data.model.todo.Todo
import com.dosystem.todo.data.model.todo.TodoImpl
import com.dosystem.todo.data.model.todo.TodoWithCategory
import com.dosystem.todo.data.repository.CategoryRepository
import com.dosystem.todo.data.repository.TodoRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TodoViewModel(
    private val categoryRepo: CategoryRepository,
    private val todoRepo: TodoRepository
): ContainerHost<TodoState, TodoSideEffect>, ViewModel() {
    override val container: Container<TodoState, TodoSideEffect> = container(TodoState())
    @NativeCoroutines
    val state: StateFlow<TodoState> = container.stateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepo.insert(CategoryModelImpl(id = 1).toEntity())
            loadCategory()
        }
        loadTodo()
        loadCategory()
    }

    fun loadTodo() = intent {
        val list = todoRepo.getAll()
        reduce { state.copy(todoList = list) }
    }

    fun loadCategory() = intent {
        val list = categoryRepo.getAll()
        reduce { state.copy(categoryList = list.map { it.toModel() }) }
    }

    fun insertTodo(todo: Todo = TodoImpl()) = intent {
        todoRepo.insert((todo as TodoImpl).copy(categoryId = 1).toEntity())
        loadTodo()
    }

    fun insertCategory(category: CategoryModel = CategoryModelImpl()) = intent {
        categoryRepo.insert((category as CategoryModelImpl).toEntity())
        loadCategory()
    }

    @OptIn(ExperimentalTime::class)
    fun updateTodo(todo: Todo) = intent {
        todoRepo.upsert(
            (todo as TodoImpl).copy(
                updatedMs = Clock.System.now().toEpochMilliseconds()
            ).toEntity()
        )
        val updatedTodo = todoRepo.getById(todo.id)
        val updatedList = todoRepo.getAll()
        reduce {
            state.copy(selectedTodo = updatedTodo, todoList = updatedList)
        }
    }

    fun updateCategory(category: CategoryModel) = intent {
        categoryRepo.getById(category.id)?.let {
            categoryRepo.upsert((category as CategoryModelImpl).copy(id = it.id).toEntity())
        }
        loadCategory()
    }

    fun deleteTodo(todo: Todo) = intent {
        todoRepo.delete((todo as TodoImpl).toEntity())
        loadTodo()
    }

    fun deleteCategory(category: CategoryModel) = intent {
        categoryRepo.delete((category as CategoryModelImpl).toEntity())
        loadCategory()
    }

    fun searchTodo(keyword: String) = intent {
        val result = if (keyword.isEmpty()) {
            todoRepo.getAll()
        } else {
            todoRepo.getByKeyword(keyword)
        }
        reduce { state.copy(todoList = result) }
    }

    fun selectTodo(todo: TodoWithCategory? = null) = intent {
        reduce { state.copy(selectedTodo = todo) }
    }
}

data class TodoState(
    var selectedTodo: TodoWithCategory? = null,
    var todoList: List<TodoWithCategory> = listOf(),
    var categoryList: List<CategoryModel> = listOf()
)

sealed interface TodoSideEffect {
}
