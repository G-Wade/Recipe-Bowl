package com.example.recipebowl

class HomeModel {
    var modelName : String? = null

    fun getName() : String {
        return modelName.toString()
    }

    fun setName(name : String) {
        this.modelName = name
    }
}