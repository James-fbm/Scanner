package com.example.scanner.ui.component.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scanner.ui.viewmodel.ProjectListItemUiModel

@Composable
fun ProjectList(
    projectListItemUiModelList: List<ProjectListItemUiModel>,
    onItemCheckedChange: (ProjectListItemUiModel) -> Unit
) {
    LazyColumn {
        items(count = projectListItemUiModelList.size) { index ->
            ProjectListItem (
                projectListItemUiModel = projectListItemUiModelList[index],
                onItemCheckedChange = onItemCheckedChange
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun ProjectListPreview() {
    val projectListItemUiModelList = listOf(
        ProjectListItemUiModel(1, "项目1", "2022-01-01", true),
        ProjectListItemUiModel(2, "项目2", "2022-02-01", false),
        ProjectListItemUiModel(3, "项目3", "2022-03-01", false),
        ProjectListItemUiModel(4, "项目4", "2022-04-01", true),
        ProjectListItemUiModel(5, "项目5", "2022-05-01", false)
    )
    ProjectList(projectListItemUiModelList) {}
}