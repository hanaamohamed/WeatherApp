package com.volvo.image_loader

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ImageLoaderInjection {
    @Binds
    abstract fun bindImageLoader(imageLoaderImpl: ImageLoaderImpl): ImageLoader
}