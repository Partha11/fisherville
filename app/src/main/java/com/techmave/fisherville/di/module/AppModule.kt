package com.techmave.fisherville.di.module

import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import androidx.work.WorkManager
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesClipboardManager(@ApplicationContext context: Context) = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun providesGson() = Gson()

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun providesGlide(@ApplicationContext context: Context): RequestManager = GlideApp.with(context)

    @Provides
    @Singleton
    fun providesContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()
}