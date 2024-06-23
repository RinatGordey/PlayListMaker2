package com.example.playlistmaker2.db.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE tasks ADD COLUMN deadline LONG")
        }
    }
}