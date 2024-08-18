package com.example.playlistmaker2.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker2.db.data.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: TrackEntity)

    @Query("SELECT * FROM favorite_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT * FROM favorite_table WHERE trackId = :trackId)")
    suspend fun isFavorite(trackId: Int): Boolean

    @Delete
    suspend fun deleteTrack(track: TrackEntity)
}