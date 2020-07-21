package com.example.mviapplication.ui.main

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mviapplication.R
import com.example.mviapplication.core.network.PicsumApi
import com.example.mviapplication.ui.common.ObservableSourceActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : ObservableSourceActivity<MainUiEvent>(), Consumer<MainViewModel> {

    private val picsumApi: PicsumApi by inject()

    private val binder by lazy { MainActivityBinder(this, MainFeature(picsumApi)) }
    private val imageAdapter = ImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        binder.setup(this)
    }

    override fun onResume() {
        super.onResume()
        onNext(MainUiEvent.GenerateButtonClicked)
    }

    override fun accept(viewModel: MainViewModel) {
        progressBar.isVisible = viewModel.isLoading
        imageAdapter.update(viewModel.images)
    }

    private fun initViews() {
        with(imageRecycler) {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }
}
