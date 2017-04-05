# RxUIKotlin

Android + UI + Kotlin + RxJava2

Made with love at the [Equisense](http://equisense.com) HQ. This library is used in our [Equisense app](https://play.google.com/store/apps/details?id=com.equisense.motion).

🛑 **Work in progress** : Destructive code is often commited on this repo, an alpha release will be released with jitpack when the library will be stable enough.

## Introduction
This library is made to manage your subscriptions when fetching data with RxJava2 inside a `Activity`, a `Fragment`, a custom `View` or a `Service`. It cancel your request depending of the state of the lifecycle by disposing your subscription (calling `dispose()` on the `Disposable` object).

Unlike [RxLifecycle](https://github.com/trello/RxLifecycle), this library doesn't transform your observable by emitting `onComplete()` when your view is gone, it just `dispose()` your `Disposable`, that's all. So you can use `Single` or `Completable` in your code without manually handle every `CancellationException`.

### Restrictions

It works only with [RxJava2](https://github.com/ReactiveX/RxJava), it's designed to work with [Kotlin](https://github.com/JetBrains/kotlin) (usage of extensions) and the auto-dispose for fragment works only with fragments from  the [support library](https://developer.android.com/topic/libraries/support-library/index.html).

## Installation

//TODO

## Usage

It's pretty simple :

```kotlin
anObservable
  .subscribe {
    //Do your stuff
  }
  .disposeOnState(FragmentState.DESTROY_VIEW, this)
```

It's exactly the same for activities :
```kotlin
disposeOnState(ActivityState.DESTROY, this)
```
custom views :
```kotlin
disposeOnState(ViewState.DETACH, this)
```
or services :
```kotlin
disposeOnState(ServiceState.DESTROY, this)
```
That's all !

## Lifecycle for custom views

Unlike `Activity`, `Fragment` or `Service` a view doesn't have any lifecycle, to simulate it, RxUIKotlin use the `View.addOnAttachStateChangeListener()` method to detect when to call `dispose()`. In addition, RxUIKotlin expose the methods `onAttach()` and `onDetach()` that you can override in your custom view.

To correctly create a custom view which can be used in the method `disposeOnState`, implement `RxViewInterface` in your custom view :
```kotlin
class MyCustomView : View, RxViewInterface  {
  //Your stuff
}
```
Override the property view and setup to this
```kotlin
class MyCustomView : View, RxViewInterface  {
  override val view: View get() = this
  //Your stuff
}
```
Finally call the method `initLifecycle()` in your `init{}` code block before any instruction :
```kotlin
class MyCustomView : View, RxViewInterface  {
  override val view: View get() = this
  init {
    initLifecycle()
    //Your code
  }
  //Your stuff
}
```
That's all ! Now your custom view can override the methods `onAttach()` and `onDetach()` and you can use the `disposeOnState(ViewState.DETACH, this)` method :
```kotlin
class MyCustomView : View, RxViewInterface  {
  override val view: View get() = this
  init {
    initLifecycle()
  }
  
  override fun onAttach() {
    anObservable
      .subscribe {
        //Update UI
      }
      .disposeOnState(ViewState.DETACH, this)
  }
}
```

//TODO 

- Make it Java compatible if possible

- Link to our twitter, github issues

- More test

- Example app

- Licence
