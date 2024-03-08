package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tb_volume",
    indices = [Index(value = ["volume_id"])],
    foreignKeys = [ForeignKey(
        entity = CollectionEntity::class,
        parentColumns = arrayOf("collection_id"),
        childColumns = arrayOf("collection_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class VolumeEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("volume_id")
    val volumeId: Int,

    @ColumnInfo("volume_name")
    val volumeName: String,

    @ColumnInfo("volume_source")
    val volumeSource: String,

    @ColumnInfo("collection_id")
    val collectionId: Int,

    @ColumnInfo("create_time")
    val createTime: Date,

    @ColumnInfo("modify_time")
    val modifyTime: Date
)