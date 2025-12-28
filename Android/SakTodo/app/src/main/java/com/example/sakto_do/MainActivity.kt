package com.example.sakto_do

import android.app.AlertDialog
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sakto_do.ui.theme.SakTodoTheme
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SakTodoTheme {
                Surface(
                    color = Color(0xE1232325),
                ) {
                    toDoListScreen();
                }
            }
        }
    }
}

@Composable
fun toDoListScreen(viewModel: TodoViewModel = viewModel()) {
    //var inplists by remember { mutableStateOf(listOf<String>()) }
    val todos by viewModel.todos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
            . fillMaxSize (),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Text(
            "TO DO",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp),
            fontSize = 50.sp,
            lineHeight = 80.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center,

            color = Color.White
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0x862A2C2F),
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(25.dp).fillMaxSize()
            ) {
                LazyColumn(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(todos) {

                            todo ->
                        todoItemCard(
                            todo = todo.text,
                            onDelete = {
                                viewModel.deleteTodo(todo)
                            });
                    }
                }
            }
        }
    }
    }
    if (showDialog) {
        AddTodoDialog(
            onAdd = {
//                inplists = inplists + newTodo
                viewModel.addTodo(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

}
@Composable
fun AddTodoDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add Todo",color = Color.Black)
        },
        text = {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Enter task") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onAdd(inputText)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun todoItemCard(
    todo: String,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            // containerColor = MaterialTheme.colorScheme.surface
            containerColor = Color(0xFF2B72AB)

        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextField(
                value = todo,
                onValueChange = { /* update logic */ },
                colors = TextFieldDefaults.colors(
                    // Make the inner box transparent so it shows the Card color behind it
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, // Hides the underline
                    unfocusedIndicatorColor = Color.Transparent
                ),
                readOnly = true
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete todo",
                    tint = Color.Red,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }

}
