package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("tb_set")
data class SetEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("set_id")
    val setId: Int,

    @ColumnInfo("set_name")
    val setName: String,

    @ColumnInfo("project_id")
    val projectId: Int,

    @ColumnInfo("create_time")
    val createTime: Date = Date(),

    @ColumnInfo("modify_time")
    val modifyTime: Date = Date()
)