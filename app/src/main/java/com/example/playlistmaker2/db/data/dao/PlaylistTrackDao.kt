package com.example.playlistmaker2.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker2.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker2.db.data.entity.TrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Delete
    suspend fun deleteTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table WHERE trackId = :id")
    suspend fun getCurrentTrack(id: Int): PlaylistTrackEntity

}