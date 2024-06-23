package com.example.playlistmaker2.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker2.db.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertPlaylist(playlist: PlaylistEntity)

}