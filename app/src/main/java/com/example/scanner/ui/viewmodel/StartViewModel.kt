package com.example.scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.scanner.data.repo.SetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val startRepository: SetRepository
): ViewModel() {

}