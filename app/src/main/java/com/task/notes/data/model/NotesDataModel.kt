package com.task.notes.data.model

import com.task.notes.data.local.NotesEntity

class NotesDataModel() {
    var title: String? = null
    var description: String? = null
    var content: String? = null
    var categoriesList: String? = null
//    var categoriesList : List<String?>?=null

    constructor(notes: NotesEntity) : this() {
        notes.apply {
            this@NotesDataModel.title = this.title
            this@NotesDataModel.description = this.description
            this@NotesDataModel.content = this.content
            this@NotesDataModel.categoriesList = this.category
        }
    }




}