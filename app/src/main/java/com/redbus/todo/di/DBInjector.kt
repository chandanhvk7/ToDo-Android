package com.redbus.todo.di

import android.app.Application

class DBInjector:Application() {
    companion object{
        val dbContainer=DBContainer()
    }
}