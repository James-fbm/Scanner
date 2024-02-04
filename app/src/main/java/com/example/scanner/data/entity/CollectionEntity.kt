package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("tb_collection")
data class CollectionEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("collection_id")
    val collectionId: Int,

    @ColumnInfo("collection_name")
    val collectionName: String,

    @ColumnInfo("project_id")
    val projectId: Int,

    @ColumnInfo("create_time")
    val createTime: Date = Date(),

    @ColumnInfo("modify_time")
    val modifyTime: Date = Date()
)