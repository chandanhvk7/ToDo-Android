package com.redbus.todo

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.redbus.todo.model.TodoItem
import com.redbus.todo.viewmodel.StdVMFactory
import com.redbus.todo.viewmodel.TodoViewModel

class AddTodoActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var buttonAddTodo: Button
    private lateinit var buttonBack: ImageButton
    private var todoItemToEdit: TodoItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_todo)
        editTitle=findViewById(R.id.editTextTitle)
        buttonAddTodo = findViewById(R.id.buttonAddTodo)
        if (intent.hasExtra("todo")) {
            todoItemToEdit = intent.getSerializableExtra("todo") as? TodoItem
            todoItemToEdit?.let {
                // Populate UI with todo item details for editing
                editTitle.setText(it.title)
            }
        }
        buttonAddTodo.setOnClickListener {
            val title = editTitle.text.toString()

            if (title.isNotEmpty()) {
                if (todoItemToEdit != null) {
                    // Update existing todo item
                    val intent = Intent()
                    intent.putExtra("todo", todoItemToEdit?.copy(title = title))
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    // Add new todo item
                    val intent = Intent()
                    intent.putExtra("todo", TodoItem(title = title))
                    setResult(Activity.RESULT_OK, intent)
                }

                // Finish activity
                finish()
            } else {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }
        buttonBack=findViewById(R.id.imageButton)
        buttonBack.setOnClickListener {
            finish()
        }

    }
}

