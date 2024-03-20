package com.redbus.todo.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewmodel: ViewModel() {
    val characterCount = MutableLiveData<Int>().apply { value = 0 }

    fun updateCharacterCount(title: String) {
        characterCount.value = title.length
    }
}