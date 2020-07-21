package com.example.mviapplication.ui.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mviapplication.R
import com.example.mviapplication.core.common.ImageLoader
import com.example.mviapplication.core.models.Photo
import com.example.mviapplication.ui.common.ObservableSourceActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.concurrent.Executors


class MainActivity : ObservableSourceActivity<MainUiEvent>(), Consumer<MainViewModel> {

    private val imageLoader: ImageLoader by inject()
    private val binder: MainActivityBinder by inject { parametersOf(this) }

    private val imageAdapter = ImageAdapter(imageLoader)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        binder.setup(this)
    }

    override fun accept(viewModel: MainViewModel) {
        progressBar.isVisible = viewModel.isLoading

        viewModel.positionalDataSource?.let { dataSource ->
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()

            val pagedList: PagedList<Photo> = PagedList.Builder(dataSource, config)
                .setFetchExecutor(Executors.newCachedThreadPool())
                .setNotifyExecutor(ContextCompat.getMainExecutor(this))
                .build()

            imageAdapter.submitList(pagedList)
        }
    }

    private fun initViews() {
        with(imageRecycler) {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }


    }
}
