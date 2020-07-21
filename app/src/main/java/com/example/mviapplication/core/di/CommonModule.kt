package com.example.mviapplication.core.di

import com.example.mviapplication.core.common.ImageLoader
import com.example.mviapplication.core.common.ImageLoaderImpl
import org.koin.dsl.module

/**
 * @author s.buvaka
 */
val commonModule = module {

    factory<ImageLoader> { ImageLoaderImpl() }
}
