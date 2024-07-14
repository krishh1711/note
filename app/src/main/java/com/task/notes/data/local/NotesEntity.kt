package com.task.notes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesEntity")
class NotesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int ? =null,
    @ColumnInfo("title") var title: String,
    @ColumnInfo("description") var description: String? = null,
    @ColumnInfo("content") var content: String,
    @ColumnInfo("category") var category: String? = null,
)

