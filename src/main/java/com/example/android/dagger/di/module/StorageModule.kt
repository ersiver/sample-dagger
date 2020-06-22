package com.example.android.dagger.di.module

import android.content.Context
import com.example.android.dagger.storage.SharedPreferencesStorage
import com.example.android.dagger.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.Provides

// Module is the way we tell Dagger how to provide Storage is different that in case of Activities
// because Storage is an interface and as such cannot be instantiated directly.
// To do this we will use a Dagger Module. Dagger Modules tell Dagger how to provide
// instances of a certain type. Dependencies are defined using the @Provides and @Binds annotations.

// Use @Binds to tell Dagger which implementation it needs to use when providing an interface.


// Tells Dagger this is a Dagger module
@Module
abstract class StorageModule {

    // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
    //With thIS above, we told Dagger "when you need a Storage object use SharedPreferencesStorage"
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage


    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. Storage).
    // Function parameters are the dependencies of this type (i.e. Context)
    // Example:
   // @Provides
   // fun provideStorage(context: Context): Storage {
        // Whenever Dagger needs to provide an instance of type Storage,
        // this code (the one inside the @Provides method) will be run.
      //  return SharedPreferencesStorage(context)
   // }
}