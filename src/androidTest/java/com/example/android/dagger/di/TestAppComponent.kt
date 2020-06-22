package com.example.android.dagger.di

import com.example.android.dagger.di.component.AppComponent
import com.example.android.dagger.di.module.SubcomponentsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestStorageModule::class, SubcomponentsModule::class ])
interface TestAppComponent : AppComponent