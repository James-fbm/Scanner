package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_volume_source",
    indices = [Index(value = ["volume_id"])],
    foreignKeys = [ForeignKey(
        entity = VolumeEntity::class,
        parentColumns = arrayOf("volume_id"),
        childColumns = arrayOf("volume_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class VolumeSourceEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("source_id")
    val sourceId: Int,

    @ColumnInfo("source_line")
    val sourceLine: String,

    @ColumnInfo("volume_id")
    val volumeId: Int

)