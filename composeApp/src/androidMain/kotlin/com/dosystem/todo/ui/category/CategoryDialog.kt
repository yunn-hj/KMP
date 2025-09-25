package com.dosystem.todo.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dosystem.todo.viewmodel.TodoViewModel
import com.dosystem.todo.ui.todo.AddItem
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White,
        shape = BottomSheetDefaults.ExpandedShape
    ) {
        LazyColumn(
            modifier = modifier
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            item {
                AddItem(modifier = modifier.fillMaxWidth()) {
                    viewModel.insertCategory()
                }
            }
            items(state.categoryList.size) { idx ->
                CategoryItem(
                    item = state.categoryList[idx],
                    todo = state.selectedTodo,
                    onTodoCategoryEdit = { todoWithCategory ->
                        if (todoWithCategory == null) return@CategoryItem
                        val todo = todoWithCategory.todo.copy(
                            categoryId = todoWithCategory.category.id
                        ).toModel()
                        viewModel.updateTodo(todo)
                    },
                    onCategoryEdit = {
                        viewModel.updateCategory(it)
                    },
                    onCategoryDelete = {
                        viewModel.deleteCategory(it)
                    }
                )
            }
        }
    }
}