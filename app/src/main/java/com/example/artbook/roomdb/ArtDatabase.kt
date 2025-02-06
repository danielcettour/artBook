package com.example.artbook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.Provides

@Database(entities = [Art::class], version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}
