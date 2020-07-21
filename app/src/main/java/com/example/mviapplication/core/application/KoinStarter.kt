package com.example.mviapplication.core.application

import android.app.Application
import com.example.mviapplication.core.di.commonModule
import com.example.mviapplication.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * @author s.buvaka
 */
class KoinStarter {

    fun start(application: Application) {
        startKoin {
            androidContext(application)
            androidLogger(level = Level.DEBUG)
            modules(modules)
        }
    }

    private val modules = listOf(
        commonModule,
        networkModule
    )
}
