package com.redbus.todo.presentation

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.redbus.todo.R
import com.redbus.todo.di.DBInjector
import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.viewmodel.AddViewmodel
import com.redbus.todo.viewmodel.StdAddVMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddTodoActivity : AppCompatActivity() {
    private lateinit var editTitle: EditText
    private lateinit var buttonAddTodo: Button
    private lateinit var buttonBack: ImageButton
    private lateinit var characterCountTextView: TextView
    private var todoItemToEdit: TodoItem? = null
    private lateinit var viewModel:AddViewmodel
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_todo)

        editTitle=findViewById(R.id.editTextTitle)
        characterCountTextView = findViewById(R.id.characterCountTextView)
        buttonAddTodo = findViewById(R.id.buttonAddTodo)
        buttonBack=findViewById(R.id.imageButton)
                viewModel = ViewModelProvider(
                    this,
                    StdAddVMFactory(this.applicationContext as Application, DBInjector.dbContainer)                    )[AddViewmodel::class.java]
                val id = intent.getIntExtra("todo",-1)
        if (id!=-1) {
                todoItemToEdit = viewModel.getById(id)
                todoItemToEdit?.let {
                    // Populate UI with todo item details for editing

                        editTitle.setText(it.title)
                        viewModel.updateCharacterCount(it.title)

            }
        }
        editTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length ?: 0 > 15) {
                    // If so, trim the text to 50 characters
                    val trimmedText = s?.substring(0, 15)
                    // Update the EditText with the trimmed text
                    editTitle.setText(trimmedText)
                    // Move the cursor to the end of the EditText
                    editTitle.setSelection(trimmedText?.length ?: 0)
                    editTitle.error="Cannot be more that 15 characters"
                }

                // Update the character count
                viewModel.updateCharacterCount(editTitle.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        viewModel.characterCount.observe(this, Observer { count ->
            characterCountTextView.text = "$count/15"
        })

        buttonAddTodo.setOnClickListener {
            val title = editTitle.text.toString()

            if (title.isNotEmpty()) {
                if(id!=-1) {
                    if (todoItemToEdit != null) {
                        // Update existing todo item
                        viewModel.update(todoItemToEdit!!.copy(title = title))
                    }
                }else {
                    // Add new todo item
                    viewModel.insert(TodoItem(title=title))
                }

                // Finish activity
                finish()
            } else {
                editTitle.error="Please enter a title"
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }

    }
}

