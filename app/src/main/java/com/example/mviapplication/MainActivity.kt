package com.example.mviapplication

import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.mviapplication.core.network.PicsumApi
import com.example.mviapplication.ui.common.ObservableSourceActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : ObservableSourceActivity<UiEvent>(), Consumer<ViewModel> {

    private val picsumApi: PicsumApi by inject()
    private val binder by lazy { MainActivityBinder(this, Feature(picsumApi)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binder.setup(this)

        generateButton.setOnClickListener { onNext(UiEvent.GenerateButtonClicked) }
    }

    override fun accept(viewModel: ViewModel) {
        imageContainer.isVisible = !viewModel.isLoading

        Glide.with(this)
            .load(viewModel.url)
            .into(imageContainer)

        progressBar.isVisible = viewModel.isLoading
    }
}
