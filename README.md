# RxUIKotlin

Android + UI + Kotlin + RxJava2

Made with love at the [Equisense](http://equisense.com) HQ. This library is used in our [Equisense app](https://play.google.com/store/apps/details?id=com.equisense.motions).

🛑 **Work in progress** : Destructive code is often commited on this repo, an alpha release will be released with jitpack when the library will be stable enough.

## Introduction
This library is made to manage your subscriptions when fetching data with RxJava2 inside a `Activity`, a `Fragment` or a `ViewHolder`. It cancel your request depending of the state of the lifecycle by disposing your subscription (calling `dispose()` on the `Disposable` object).

Unlike [RxLifecycle](https://github.com/trello/RxLifecycle), this library doesn't transform your observable by emitting `onComplete()` when your view is gone, it just `dispose()` your `Disposable`, that's all. So you can use `Single` or `Completable` in your code without manually handle every `CancellationException`.

Unlike [android-disposebag](https://github.com/kizitonwose/android-disposebag), this library doesn't use [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) to listen the lifecycle because the class [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle.html) from [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) doesn't emit states like `PRE_ATTACH`, `ATTACH`, `ACTIVITY_CREATED`, `VIEW_CREATED`, `DESTROY_VIEW`, `SAVE_INSTANCE_STATE`, `DESTROY_VIEW` and `DETACH` while RxUIKotlin does.

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

ViewHolder :
```kotlin
override fun onAttach(detachDisposable: CompositeDisposable) {
  anObservable
    .subscribe {
      //Do your stuff
    }
    .addTo(detachDisposable)
}
```

That's all !

//TODO 

- Make it Java compatible if possible

- Link to our twitter, github issues

- More test

- Example app

- Licence
