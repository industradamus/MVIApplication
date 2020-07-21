package com.example.mviapplication.ui.main

import androidx.paging.PositionalDataSource
import com.example.mviapplication.core.models.Photo

/**
 * @author s.buvaka
 */
class MainViewModel(
    val isLoading: Boolean,
    val positionalDataSource: PositionalDataSource<Photo>?
)
