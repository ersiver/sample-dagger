package com.example.android.dagger.di.component

import com.example.android.dagger.di.scope.ActivityScope
import com.example.android.dagger.login.LoginActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface LoginComponent {

    //Factory to create an instance of this interface
    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: LoginActivity)
}