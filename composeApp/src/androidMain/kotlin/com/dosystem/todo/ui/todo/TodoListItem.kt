package com.dosystem.todo.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dosystem.todo.AndroidDueDate
import com.dosystem.todo.data.model.todo.Todo
import com.dosystem.todo.data.model.todo.TodoWithCategory
import com.dosystem.todo.getColor
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun AddItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier.clickable(true) { onClick() }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }

    TextField(
        value = input,
        onValueChange = {
            input = it
            onSearch(input)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = "검색어를 입력하세요",
                color = Color.LightGray,
                fontSize = 14.sp
            )
        },
        shape = RectangleShape,
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier =  modifier.fillMaxWidth()
    )
}

@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    todoWithCategory: TodoWithCategory,
    onTodoChange: (Todo) -> Unit,
    onTodoDelete: (Todo) -> Unit,
    onCategoryClick: () -> Unit,
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onTodoDelete(todoWithCategory.todo.toModel())
            }
            it != SwipeToDismissBoxValue.EndToStart
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
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
        },
        content = {
            TodoForegroundContent(
                todoWithCategory = todoWithCategory,
                onTodoChange = onTodoChange,
                onCategoryClick = onCategoryClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForegroundContent(
    modifier: Modifier = Modifier,
    todoWithCategory: TodoWithCategory,
    onTodoChange: (Todo) -> Unit,
    onCategoryClick: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        TodoDatePicker(
            selectedMs = todoWithCategory.todo.dueMs,
            onDateChange = {
                val prevDueDate = AndroidDueDate(todoWithCategory.todo.dueMs)
                val updatedDueDate = AndroidDueDate(it)
                val updatedDueMs = LocalDateTime.of(
                    updatedDueDate.year, updatedDueDate.month, updatedDueDate.day,
                    prevDueDate.hour, prevDueDate.minute
                ).toInstant(ZoneOffset.UTC).toEpochMilli()

                onTodoChange(
                    todoWithCategory.todo.copy(dueMs = updatedDueMs).toModel()
                )
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        TodoTimePicker(
            selectedMs = todoWithCategory.todo.dueMs,
            onTimeChange = { hour, min ->
                val prevDueDate = AndroidDueDate(todoWithCategory.todo.dueMs)
                val updatedDueMs = LocalDateTime.of(
                    prevDueDate.year, prevDueDate.month, prevDueDate.day,
                    hour, min
                ).toInstant(ZoneOffset.of("+9")).toEpochMilli()

                onTodoChange(
                    todoWithCategory.todo.copy(dueMs = updatedDueMs).toModel()
                )
            },
            onDismiss = { showTimePicker = false }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 2.dp)
    ) {
        Checkbox(
            checked = todoWithCategory.todo.isCompleted,
            colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = Color(0xFF739EC9),
                checkedBorderColor = Color(0xFF739EC9),
                uncheckedBorderColor = Color.LightGray
            ),
            onCheckedChange = { isChecked ->
                onTodoChange(
                    todoWithCategory.todo.copy(isCompleted = isChecked).toModel()
                )
            }
        )
        Column(modifier = modifier.weight(1f)) {
            Text(
                text = todoWithCategory.category.name,
                fontSize = 10.sp,
                color = getColor(todoWithCategory.category.color),
                modifier = modifier.clickable(true) { onCategoryClick() }
            )
            TodoInput(todoWithCategory = todoWithCategory) { onTodoChange(it) }
            TodoDate(
                todoWithCategory = todoWithCategory,
                onDateClick = { showDatePicker = true },
                onTimeClick = { showTimePicker = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoInput(
    modifier: Modifier = Modifier,
    todoWithCategory: TodoWithCategory,
    onTodoChange: (Todo) -> Unit
) {
    val prevContentInput = todoWithCategory.todo.content
    var contentInput by remember(todoWithCategory.todo.id) {
        mutableStateOf(todoWithCategory.todo.content)
    }
    var isContentEditing by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = contentInput,
            onValueChange = { contentInput = it },
            singleLine = true,
            modifier = modifier
                .onFocusChanged { isContentEditing = it.hasFocus }
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
                    value = contentInput,
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
                            text = "새로운 할일",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus(true)
                onTodoChange(todoWithCategory.todo.copy(content = contentInput).toModel())
            }
        )
        if (isContentEditing) {
            TodoEditingButtons(
                onCancel = {
                    focusManager.clearFocus(true)
                    contentInput = prevContentInput
                },
                onConfirm = {
                    focusManager.clearFocus(true)
                    onTodoChange(todoWithCategory.todo.copy(content = contentInput).toModel())
                }
            )
            Spacer(modifier.width(15.dp))
        }
    }
}

@Composable
fun TodoDate(
    modifier: Modifier = Modifier,
    todoWithCategory: TodoWithCategory,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        with(AndroidDueDate(todoWithCategory.todo.dueMs)) {
            Text(
                text = this.dueDate,
                modifier = modifier.clickable(true) { onDateClick() },
                fontSize = 10.sp,
                color = Color.LightGray
            )
            Spacer(modifier.width(10.dp))
            Text(
                text = this.dueTime,
                modifier = modifier.clickable(true) { onTimeClick() },
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun TodoEditingButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    IconButton(onClick = onCancel, modifier = modifier.then(modifier.size(24.dp))) {
        Icon(Icons.Default.Close, contentDescription = null)
    }
    Spacer(modifier.width(10.dp))
    IconButton(onClick = onConfirm, modifier = modifier.then(modifier.size(24.dp))) {
        Icon(Icons.Default.Check, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDatePicker(
    modifier: Modifier = Modifier,
    selectedMs: Long,
    onDateChange: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState(initialDisplayedMonthMillis = selectedMs)

    DatePickerDialog(
        colors = DatePickerDefaults.colors().copy(
            containerColor = Color.White,
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(
                onClick = {
                    onDateChange(state.selectedDateMillis ?: 0)
                    onDismiss()
                },
                colors = IconButtonDefaults.iconButtonColors().copy(contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
            }
        },
        dismissButton = {
            IconButton(
                onClick = onDismiss,
                colors = IconButtonDefaults.iconButtonColors().copy(contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    ) {
        DatePicker(
            state = state,
            colors = DatePickerDefaults.colors().copy(
                containerColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTimePicker(
    modifier: Modifier = Modifier,
    selectedMs: Long,
    onDismiss: () -> Unit,
    onTimeChange: (hour: Int, min: Int) -> Unit
) {
    val dueDate = AndroidDueDate(selectedMs)
    val state = rememberTimePickerState(
        initialHour = dueDate.hour,
        initialMinute = dueDate.minute,
        is24Hour = false
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text("시간 선택", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier.height(30.dp))
            TimePicker(state)
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
                Spacer(modifier.width(10.dp))
                IconButton(onClick = {
                    onTimeChange(state.hour, state.minute)
                    onDismiss()
                }) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }
}