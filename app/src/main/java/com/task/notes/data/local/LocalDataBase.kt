package com.task.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NotesEntity::class],
    version = 1
)
abstract class LocalDataBase : RoomDatabase() {
    abstract fun dao(): NoteDao
}