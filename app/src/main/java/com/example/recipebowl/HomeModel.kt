package com.example.recipebowl

class HomeModel {
    var modelID : String? = null
    var modelName : String? = null

    fun getName() : String {
        return modelName.toString()
    }

    fun setName(name : String) {
        this.modelName = name
    }

    fun setID(id: String) {
        this.modelID = id
    }
}