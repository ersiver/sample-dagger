# sample-dagger
This app was built following AndroidDevelopers Codelabs. The app demonstrates :

+ How to create an Application graph using Dagger <b>@Component</b> annotation.
+ How to add information to the graph using <b>@Inject, @Module, @Binds and @BindsInstance</b> annotations.
+ How to create flow containers using <b>@Subcomponent</b>.
+ How to reuse instances of objects in different containers using <b>Scopes</b>.
+ Dagger Qualifiers and <b>@Provides</b> annotation.
+ How to <b>test</b> your application that uses Dagger with unit and instrumentation tests.


# Cheat sheet

## 1. Dependency injection 

(DI) is a technique to provide the class with an Object without the class and Object been tightly coupled (since that can cause the app difficult to test and the code not reusable). 

<b>Tightly coupled:</b> the class creates and initializes its own instance of the Object. Dependency injection: class had the Object supplied as a parameter. The app can provide these dependencies when the class is constructed or pass them in to the functions that need each dependency.

<b>There are two major ways to do dependency injection in Android:</b>
+ <b>Constructor Injection</b>: you pass the dependencies of a class to its constructor.
+ <b>Field Injection</b> (or Setter Injection) Certain Android framework classes such as activities and fragments are instantiated by the system, so constructor injection is not possible. With field injection, dependencies are instantiated after the class is created.

### 1.1. Manual dependency
when the programmer write the code to created, provided, and managed the dependencies of the different classes, without relying on a library it’s Manual dependency. 
Manual dependency injection presents several problems:
* For big apps, taking all the dependencies and connecting them correctly can require a large amount of boilerplate code. 
* When you're not able to construct dependencies before passing them in — for example when using lazy initializations or scoping objects to flows of your app — you need to write and maintain a custom container (or graph of dependencies) that manages the lifetimes of your dependencies in memory.

### 1.2. Libraries
There are libraries that solve this problem by automating the process of creating and providing dependencies like Dagger. 

<b>Dagger</b> generates code similar to what you would have written manually. It  automatically does all of this at build time as long as you declare dependencies of a class and specify how to satisfy them using annotation. 

<b>Benefits of using Dagger</b>
Dagger frees you from writing tedious and error-prone boilerplate code by:
+ Generating the AppContainer code (application graph) that you manually implemented in the manual DI section.
+ Creating factories for the classes available in the application graph. This is how dependencies are satisfied internally.
+ Reusing a dependency or creating new instances of a type depending on how you the configuration of the type using scopes.
+ Creating containers for specific flows as you did with the login flow in the previous section using Dagger subcomponents. This improves your app's performance by releasing objects in memory when they're no longer needed.
+ The code is generated at compile time, it's traceable and more performant than other reflection-based solutions.

### 1.3. Alternatives to DI
An alternative to dependency injection is using a service locator. The service locator design pattern also improves decoupling of classes from concrete dependencies.


## 2. Dagger

### 2.1. Graph of the dependencies 
+ Dagger creates a graph that knows how to provide an instance of a class, and recursively, of its dependencies. Dagger knows how to do this because of the <b>@Inject</b> annotation on the classes' constructor. For every class in the graph, Dagger generates a factory-type class that it uses internally to get instances of that type.

+ At build time, Dagger walks through your code and:<br>
a) Builds and validates dependency graphs, ensuring that: every object's dependencies can be satisfied, so there are no runtime exceptions and also that no dependency cycles exist, so there are no infinite loops.<br>
b) Generates the classes that are used at runtime to create the actual objects and their dependencies.<br>

+ <b>There are two different ways to interact with the Dagger graph:</b><br>
a) Exposing a function that  takes a class as a parameter and <b>returns Unit</b> -> To tell Dagger about an object (Activity/ fragment) that requires a dependency to be injected (e.g. fun inject(activity: MainActivity)).<br>
b) Exposing a function with the <b>return type</b> -> To get objects from the graph(e.g. fun repository(): UserRepository).<br>

+ You usually keep an instance of component interface in your custom <b>application class</b> because you want an instance of the graph to be in memory as long as the app is running (also because Of the context availability). In this way, the graph is attached to the app lifecycle. 

### 2.2. @Inject annotation
+ Add this annotation to the class constructor to let Dagger know how to create instances of this object. 
+ And recursively annotate the class(es) passed to the constructor, so Dagger knows how to create them, too. 

### 2.3. @Component annotation
+ To make Dagger create a graph of the dependencies in the project , you need to create an interface and annotate it with @Component. 
+ Inside the @Component interface, define functions that return instances of the classes you need. @Component tells Dagger to generate a container with all the dependencies required to satisfy the types it exposes.

### 2.4. Constructor Injection with Dagger
+ Annotate the constructor of classes with: @Inject constructor(val param Param). That is what is called constructor injection.
+ The constructor parameters will be the dependencies of that type.
+ Dagger will create them for you.
+ <b>When @Inject is annotated on a class constructor, it's telling Dagger how to provide instances of that class.</b>

### 2.5. Field Injection with Dagger
Android framework classes such as activities and fragments are instantiated by the system, so the constructor injection is not a case. Instead, you have to use field injection:
+ Annotate the  fragment’s and/or activity’s of fields with @inject. Important: Dagger-injected fields cannot be private. They need to have at least <b>package-private visibility</b>. Provide injection to the fields respectively.
+ Create interface AppComponent and annotate it with @Component. Create method(s) pass Object of the class, that is to be injected as parameter (e.g. fun inject(activity: RegistrationActivity). Dagger will create a Container as we would have done with manual dependency injection. A @Component interface gives the information Dagger needs to generate the graph at compile-time. The parameter of the interface methods define what classes request injection.
+ Inside the Application create an instance of the AppComponent that will be used by all the Activities in the project (we want the graph to be in memory as long as the app is running. In this way, the graph is attached to the app's lifecycle. 
+ To inject an object in the activity, you'd use the appComponent defined in your application class and call the inject() method, passing in an instance of the activity that requests injection. 
Inject Dagger in the Activity's onCreate method <b>before calling super.onCreate()</b> to avoid issues with fragment restoration. 
In super.onCreate, an Activity during the restore phase will attach fragments that might want to access activity bindings. 
For Fragments, override <b>onAttach()</b> and inject Components after calling super.onAttach.
+  @Inject annotated fields will be provided by Dagger.
+ <b>When i@Inject is annotated on a class field, it's telling Dagger that it needs to populate the field with an instance of that type.</b>

### 2.6. Application context
+ In the AppCompat interface create interface Factory annotated with @Component.Factory
+ using the @BindsInstance annotation create instances of the AppComponent.
+ in Application Create an instance of AppComponent using its Factory constructor.
+ <b>@BindsInstance</b> tells Dagger that it needs to add that instance in the graph and whenever Context is required, provide that instance.

### 2.7. How to tell Dagger about interfaces type or classes that project doesn't own?
<b>Modules</b> are a way to semantically encapsulate information on how to provide objects
+ Modules are another way to tell Dagger how to provide an instance of a class of certain types. E.g. interfaces or classes that project doesn't own (e.g. an instance of Retrofit,  an OkHttpClient, or how to configure Gson or Moshi ). 
+ For implementation of interfaces, the best practice is using <b>@Binds</b>.  @Binds must annotate an abstract function. The return type of the abstract function is the interface we want to provide an implementation. 
+ Use the <b>@Provides</b> annotation in to tell Dagger how to provide classes that the project doesn't own (e.g. an instance of Retrofit). Function parameters are the dependencies of this type. Whenever Dagger needs to provide an instance of type LoginRetrofitService, the code inside the @Provides method is run:
+ In order for the Dagger graph to know about this module, you have to add it to the @Component interface

### 2.8. Scoping a type to the component's lifecycle 
+ Scoping a type is a way to <b>reuse</b> the same instance of the object rather than creating a new instance every time it needs to be provided. It’s convenient when the object is very expensive to create (e.g. JSON parser) or when we need to share the same instance among  multiple activities that have this type as dependency.
+ To reuse the unique instance of dependency in a container we need to use <b>scope annotation</b> of the same name inside the component interface and the class object under the scope. In that way we inform Dagger that classes annotated with this annotation are bound to the life of the graph. 

### 2.9. Subcomponents
+ In certain situations the unique instance of some object needs to be used only for the certain flow rather than staying in memory across the whole application. In that case we can structure app to create different Dagger subgraphs depending on the flow of an app, like on the picture below.
+ Subcomponents are components that <b>inherit and extend</b> the object graph of a parent component. Thus, all objects provided in the parent component will be provided in the subcomponent too. In this way, an object from a subcomponent can depend on an object provided by the parent component.
+ The app represented by the graph below consists of 4 different flows working with Dagger (implemented as Activities):
<br><b>Registration:</b> The user can register by entering username, password and accepting our terms and conditions.
<br><b>Login:</b> The user can log in using the credentials added during the registration flow and can also unregister from the app.
<br><b>Home:</b> The user is welcomed and can see how many unread notifications they have.
<br><b>Settings:</b> The user can log out and refresh the number of unread notifications (which produces a random number of notifications).

![app_graph](https://user-images.githubusercontent.com/58771510/85317054-aa738880-b4b5-11ea-9b11-5a5d86fd9023.png)

<b>AppComponent:</b>
+ AppComponent is annotated with @Component and that's the parent component. It includes two modules, StorageModule and AppSubcomponents.
+ An instance of parent component is kept in custom application class because we want an instance of the graph to be in memory as long as the app is running (also because Of the context availability). In this way, the graph is attached to the app lifecycle. 
+ UserManager class is scoped to AppComponent, so we’ll have a unique instance of it in the application graph across the whole app.

<b>UserComponent:</b>
+ Subcomponent of AppComponent.
+ Injected in Activities that happens after the user is logged in (MainActivity and SettingsActivity)
+ The UserDataRepository is scoped to that component (we don't want to keep the same instance of UserDataRepository in memory. That data is specific to a logged in user).
+ The lifetime of this Component is attached to the UserManager class(an instance of UserComponent is in UserManager). The user will be logged in if UserComponent is not null. When the user logs out, we remove the instance of UserComponent. In this way, since UserComponent contains all the data and instances of classes related to a specific user, when the user logs out, we destroy the component and all the data will be removed from memory.

<b>RegistrationComponent</b>
+ Subcomponent of AppComponent.
+ Injected into RegistrationActivity, EnterDetailsFragment and TermsAndConditionsFragment. 
+ Attached to RegistrationActivity (for every new Activity, we'll create a new RegistrationComponent and Fragments that can use that instance of RegistrationComponent).
+ RegistrationViewModel is scoped to the RegistrationComponent (RegistrationComponent will always provide the same instance of RegistrationViewModel. But we also will have a new instance of RegistrationViewModel whenever there's a new registration flow).

<b>LoginComponent </b>
+ LoginComponent is injected into LoginActivity. 

+ <b>When building the Dagger graph for your application:</b><br>
a) When you create a component, you should consider what element is responsible for the lifetime of that component. In this case, the application class was in charge of ApplicationComponent and LoginActivity in charge of LoginComponent.<br>
b) Use scoping only when it makes sense. Overusing scoping can have a negative effect on your app's runtime performance: the object is in memory as long as the component is in memory and getting a scoped object is more expensive. When Dagger provides the object, it uses DoubleCheck locking instead of a factory-type provider.


### 2.10. Testing
+ Unit Tests: You don't have to use Dagger-related code for unit tests. When testing a class that uses constructor injection, you don't need to use Dagger to instantiate that class. You can directly call its constructor passing in fake or mock dependencies directly just as you would if they weren't annotated.

+ All unit tests remain the same as with manual dependency injection except one. When we added the UserComponent.Factory to UserManager, we broke its unit tests. We have to mock what Dagger would return when calling create() on the factory

+ For integration tests, a good practice is to create a TestApplicationComponent meant for testing. Production and testing use a different component configuration.

+ You have to add the dagger annotation processor artifact to androidTest as follows:
dependencies {
    ...
    kaptAndroidTest "com.google.dagger:dagger-compiler:$dagger_version"
}

## License
Copyright 2019 Google, Inc (all resources are from Codelabs Google Developers).
