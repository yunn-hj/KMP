package com.dosystem.todo.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dosystem.todo.notifyTodo
import com.dosystem.todo.viewmodel.TodoViewModel
import com.dosystem.todo.ui.category.CategoryDialog
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    var isDialogVisible by remember { mutableStateOf(false) }

    if (isDialogVisible) {
        CategoryDialog(onDismiss = { isDialogVisible = false })
    }

    LaunchedEffect(state) {
        notifyTodo(context, state.todoList)
    }

    LazyColumn(modifier = modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                SearchItem(modifier = modifier.weight(1f)) {
                    viewModel.searchTodo(it)
                }
                AddItem(modifier.padding(end = 10.dp)) { viewModel.insertTodo() }
            }
        }
        items(state.todoList.size) { idx ->
            TodoItem(
                todoWithCategory = state.todoList[idx],
                onTodoChange = { todo -> viewModel.updateTodo(todo) },
                onTodoDelete = { todo -> viewModel.deleteTodo(todo) },
                onCategoryClick = {
                    viewModel.selectTodo(state.todoList[idx])
                    isDialogVisible = true
                }
            )
            Spacer(modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    TodoListScreen()
}