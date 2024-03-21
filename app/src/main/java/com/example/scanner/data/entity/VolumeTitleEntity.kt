package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_volume_title",
    indices = [Index(value = ["volume_id"])],
    foreignKeys = [ForeignKey(
        entity = VolumeEntity::class,
        parentColumns = arrayOf("volume_id"),
        childColumns = arrayOf("volume_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class VolumeTitleEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("title_id")
    val titleId: Int,

    @ColumnInfo("title_line")
    val titleLine: String,

    @ColumnInfo("index_line")
    val indexLine: String,

    @ColumnInfo("volume_id")
    val volumeId: Int
)