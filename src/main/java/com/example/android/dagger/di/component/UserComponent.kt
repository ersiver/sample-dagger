package com.example.android.dagger.di.component

import com.example.android.dagger.di.scope.LoggedUserScope
import com.example.android.dagger.main.MainActivity
import com.example.android.dagger.settings.SettingsActivity
import dagger.Subcomponent


// Scope annotation that the UserComponent uses
// Classes annotated with @LoggedUserScope will have a unique instance in this Component

@LoggedUserScope
@Subcomponent
interface UserComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create() : UserComponent
    }

    fun inject(activity: SettingsActivity)
    fun inject(activity: MainActivity)
}