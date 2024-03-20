package com.redbus.todo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult.*
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import com.redbus.todo.model.TodoItem
import com.redbus.todo.ui.theme.ToDoTheme
import com.redbus.todo.viewmodel.StdVMFactory
import com.redbus.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels()
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getSerializableExtra("todo") as? TodoItem
            if (data != null) {
                viewModel.insert(data)
            }
//            data?.getStringExtra("MESSAGE")?.let { Log.d("Message", it) }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner=LocalViewModelStoreOwner.current!!
                    val viewModel = ViewModelProvider(
                        owner,
StdVMFactory(LocalContext.current.applicationContext as Application)                    )[TodoViewModel::class.java]
                    TodoListScreen(todoViewModel = viewModel,this,resultLauncher)
                }
            }
        }
    }
}
@Composable
fun MyCustomAppBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Adjust height as needed
            .padding(start = 16.dp, end = 16.dp), // Add padding as needed
        contentAlignment = androidx.compose.ui.Alignment.CenterStart
    ) {


        Text(
            text = title
        )


    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalWearMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalUnitApi::class
)
@Composable
fun TodoListScreen(todoViewModel: TodoViewModel,context: Context,resultLauncher: ActivityResultLauncher<Intent>) {
    val todoItems by todoViewModel.allTodo.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var dismissState: DismissState? = null
    fun dismissed(todoItem: TodoItem){
        coroutineScope.launch {
            val snackResult = snackbarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "Undo",
                SnackbarDuration.Short
            )
            when (snackResult) {
                androidx.compose.material.SnackbarResult.Dismissed -> {}
                androidx.compose.material.SnackbarResult.ActionPerformed -> {
                    todoViewModel.insert(todoItem)
                    dismissState?.reset()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MyCustomAppBar(title = "Todo List")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to AddTodoScreen
                    val intent = Intent(context, AddTodoActivity::class.java)
                    resultLauncher.launch(intent)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Todo")
            }
        }){
        Surface(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(key = {todoItem -> todoItem.id}, items =  todoItems.reversed()) { todoItem ->

                    dismissState = rememberDismissState(
                        confirmValueChange = {
                            when(it) {
                                DismissValue.DismissedToStart -> {
                                    todoViewModel.delete(todoItem)
                                    dismissed(todoItem)
                                    true
                                }
                                else -> true
                            }
                        }
                    )
//
                    dismissState?.let { dismissState ->
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .padding(vertical = 1.dp),
                            directions = setOf(
                                DismissDirection.EndToStart
                            ),
                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.Transparent
                                        else -> Color.LightGray
                                    }
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                if (dismissState.currentValue == DismissValue.Default) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .background(color)
                                            .padding(horizontal = Dp(20f)),
                                        contentAlignment = alignment
                                    ) {
                                        Icon(
                                            icon,
                                            contentDescription = "Delete Icon",
                                            modifier = Modifier.scale(scale)
                                        )
                                    }
                                }
                            },
                            dismissContent = {

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dp(50f))
                                        .align(alignment = Alignment.CenterVertically)
                                ) {
                                    TodoItemRow(todoItem = todoItem, todoViewModel, onItemClick = {
                                        val intent = Intent(context, AddTodoActivity::class.java)
                                        intent.putExtra("todo", todoItem)
                                        resultLauncher.launch(intent)
                                    }, onDeleteClick = {
                                        val deletedItem = it
                                        todoViewModel.delete(it)
                                        coroutineScope.launch {
                                            val snackResult = snackbarHostState.showSnackbar(
                                                message = "Task deleted",
                                                actionLabel = "Undo",
                                                SnackbarDuration.Short
                                            )
                                            when (snackResult) {
                                                androidx.compose.material.SnackbarResult.Dismissed -> {}
                                                androidx.compose.material.SnackbarResult.ActionPerformed -> todoViewModel.insert(
                                                    deletedItem
                                                )
                                            }
                                        }
                                    })
                                }
                            }
                        )
                    }
                }
            }
        }
    }



}



@Composable
fun TodoItemRow(todoItem: TodoItem,viewModel: TodoViewModel,onItemClick:(TodoItem)->Unit,onDeleteClick: (TodoItem) -> Unit ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox to mark todo item as done/undone
            Checkbox(
                checked = todoItem.isDone,
                onCheckedChange = { isChecked ->
                    val updatedTodoItem = todoItem.copy(isDone = isChecked)
                    viewModel.update(updatedTodoItem)
                }
            )


            Box(
                modifier = Modifier
                    .clickable { onItemClick(todoItem) }
                    .weight(1f)// Call onItemClick when clicked
            ) {
                if (todoItem.isDone){
                    Text(
                        text = todoItem.title,
                        textDecoration = TextDecoration.LineThrough,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                } else {
                    Text(
                        text = todoItem.title,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }



                }
            Box(
                modifier = Modifier.clickable { onItemClick(todoItem) }
            ) {
                IconButton(
                    onClick = { onItemClick(todoItem) }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Update Todo"
                    )
                }
            }
            Box(
                modifier = Modifier.clickable { onDeleteClick(todoItem) }
            ) {
                IconButton(
                    onClick = { onDeleteClick(todoItem) }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Todo"
                    )
                }
            }


        }
    }




