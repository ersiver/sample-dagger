package com.example.android.dagger.di.module

import com.example.android.dagger.di.component.LoginComponent
import com.example.android.dagger.di.component.RegistrationComponent
import com.example.android.dagger.di.component.UserComponent
import dagger.Module

@Module(subcomponents = [RegistrationComponent::class, LoginComponent::class, UserComponent::class])
class SubcomponentsModule