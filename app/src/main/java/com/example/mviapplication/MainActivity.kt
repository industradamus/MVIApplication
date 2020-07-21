package com.example.mviapplication

import android.os.Bundle
import androidx.core.view.isVisible
import com.example.mviapplication.baseactivity.ObservableSourceActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ObservableSourceActivity<UiEvent>(), Consumer<ViewModel> {

    private val binder by lazy { MainActivityBinder(this, Feature()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binder.setup(this)

        generateButton.setOnClickListener { onNext(UiEvent.GenerateButtonClicked) }
    }

    override fun accept(viewModel: ViewModel) {
        generatedText.text = viewModel.text
        generatedText.isVisible = !viewModel.isLoading

        progressBar.isVisible = viewModel.isLoading
    }
}
