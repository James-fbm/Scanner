package com.example.scanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("tb_project")
data class ProjectEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("project_id")
    val projectId: Int,

    @ColumnInfo("project_name")
    val projectName: String,

    @ColumnInfo("create_time")
    val createTime: Date,

    @ColumnInfo("modify_time")
    val modifyTime: Date
)