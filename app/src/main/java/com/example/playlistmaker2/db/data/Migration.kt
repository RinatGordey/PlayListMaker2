package com.example.playlistmaker2.db.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1 {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE tasks ADD COLUMN deadline LONG")
        }
    }
}

object Migration2 {
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS playlist_track_table " +
                    "(trackId INTEGER PRIMARY KEY, trackName TEXT, artistName TEXT)")
        }
    }
}

object Migration3 {
    val MIGRATION_1_3 = object : Migration(1, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS playlist_track_table " +
                    "(trackId INTEGER PRIMARY KEY, trackName TEXT, artistName TEXT)")
        }
    }
}