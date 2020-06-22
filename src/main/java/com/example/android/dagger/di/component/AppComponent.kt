package com.example.android.dagger.di.component

import android.content.Context
import com.example.android.dagger.di.module.StorageModule
import com.example.android.dagger.di.module.SubcomponentsModule
import com.example.android.dagger.main.MainActivity
import com.example.android.dagger.user.UserManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Scope annotation that the AppComponent uses
// Classes annotated with @Singleton will have a unique instance in this Component

@Singleton
@Component(modules = [StorageModule::class, SubcomponentsModule::class])
interface AppComponent {

    //Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Types that can be retrieved from the graph
    fun registrationComponent(): RegistrationComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager
}