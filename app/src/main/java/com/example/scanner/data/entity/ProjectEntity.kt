package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("project")
data class ProjectEntity (
    @PrimaryKey
    @ColumnInfo("project_id")
    val projectId: Int,

    @ColumnInfo("project_name")
    val projectName: String,

    @ColumnInfo("create_time")
    val createTime: String,

    @ColumnInfo("modify_time")
    val modifyTime: String
)