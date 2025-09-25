package com.dosystem.todo.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosystem.todo.data.model.category.CategoryModel
import com.dosystem.todo.data.model.category.CategoryModelImpl
import com.dosystem.todo.data.model.todo.TodoWithCategory
import com.dosystem.todo.getColor


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    item: CategoryModel,
    todo: TodoWithCategory? = null,
    onTodoCategoryEdit: (TodoWithCategory?) -> Unit,
    onCategoryDelete:(CategoryModel) -> Unit,
    onCategoryEdit: (CategoryModel) -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onCategoryDelete(item)
            }
            it != SwipeToDismissBoxValue.EndToStart
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                CategoryDeleteBackgroundContent()
            }
        },
        content = {
            CategoryForegroundContent(
                modifier = modifier.selectableGroup(),
                item = item,
                todo = todo,
                onTodoCategoryEdit = onTodoCategoryEdit,
                onCategoryEdit = { isEditing, category ->
                    if (!isEditing) onCategoryEdit(category)
                },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryForegroundContent(
    modifier: Modifier = Modifier,
    item: CategoryModel,
    todo: TodoWithCategory? = null,
    onTodoCategoryEdit: (TodoWithCategory?) -> Unit,
    onCategoryEdit: (Boolean, CategoryModel) -> Unit,
) {
    val prevInput = item.name
    var isEditing by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf(item.name) }
    var color by remember { mutableStateOf(item.color) }
    val isSelected = item.id == todo?.category?.id
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(isEditing) {
        if (!isEditing) {
            val updatedCategory = (item as CategoryModelImpl).copy(name = input, color = color)

            onCategoryEdit(false, updatedCategory)
            if (item.id == todo?.category?.id) {
                onTodoCategoryEdit(todo.copy(category = updatedCategory.toEntity()))
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable(true) { isEditing = true }
                .padding(vertical = 10.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = {
                    onTodoCategoryEdit(
                        todo?.copy(
                            todo = todo.todo.copy(categoryId = item.id),
                            category = (item as CategoryModelImpl).toEntity()
                        )
                    )
                }
            )
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                singleLine = true,
                enabled = isEditing,
                textStyle = TextStyle.Default.copy(
                    color = getColor(color)
                ),
                modifier = modifier
                    .weight(1f)
                    .indicatorLine(
                        enabled = false,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors().copy(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        focusedIndicatorLineThickness = 0.dp,
                        unfocusedIndicatorLineThickness = 0.dp
                    ),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = input,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        contentPadding = PaddingValues(0.dp),
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        shape = TextFieldDefaults.shape,
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(
                                text = "새로운 카테고리",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            )
            if (isEditing) {
                IconButton(
                    onClick = {
                        input = prevInput
                        isEditing = false
                    }
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        isEditing = false
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        }
        if (isEditing) {
            CategoryColorPalette(
                selectedColor = item.color
            ) {
                color = it
            }
        }
    }
}

@Composable
fun CategoryEditBackgroundContent(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = null,
        tint = Color.White,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green)
            .wrapContentSize(Alignment.CenterStart)
            .padding(start = 10.dp)
    )
}

@Composable
fun CategoryDeleteBackgroundContent(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        tint = Color.White,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red)
            .wrapContentSize(Alignment.CenterEnd)
            .padding(end = 10.dp)
    )
}