package com.example.android.dagger.di.component

import com.example.android.dagger.di.scope.ActivityScope
import com.example.android.dagger.registration.RegistrationActivity
import com.example.android.dagger.registration.enterdetails.EnterDetailsFragment
import com.example.android.dagger.registration.termsandconditions.TermsAndConditionsFragment
import dagger.Subcomponent

//This it to ensure the same instance is injected for the Activity and Fragments.

// We want different instances of RegistrationViewModel for different registration flows.
// If the user registers and unregisters, we don't want the data from the previous
// registration to be present, hence we do not use singleton.
// We want the registration Fragments to reuse the same ViewModel coming from the Activity,
// but if the Activity changes, we want a different instance. We need to scope
// RegistrationViewModel to RegistrationActivity,

// Classes annotated with @ActivityScope will have a unique instance in this Component

@ActivityScope
@Subcomponent
interface RegistrationComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: RegistrationActivity)
    fun inject(fragment: EnterDetailsFragment)
    fun inject(fragment: TermsAndConditionsFragment)

}