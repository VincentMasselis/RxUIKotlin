# RxUIKotlin

[ ![Download](https://api.bintray.com/packages/vincentmasselis/maven/rx-ui-kotlin/images/download.svg) ](https://bintray.com/vincentmasselis/maven/rx-ui-kotlin/_latestVersion)

Android UI + Kotlin + RxJava2

Made with love at the [Equisense](http://equisense.com) HQ. This library is used in our [Equisense app](https://play.google.com/store/apps/details?id=com.equisense.motions) since a few years.

## Introduction
This library is made to manage your subscriptions when fetching data with RxJava2 inside a `Activity`, a `Fragment` or a `ViewHolder`. It cancel your request depending of the state of the lifecycle by disposing your subscription (calling `dispose()` on the `Disposable` object returned by the `subscribe` methods).

Unlike [RxLifecycle](https://github.com/trello/RxLifecycle), this library doesn't transform your observable by emitting `onComplete()` when your view is gone, it just `dispose()` your `Disposable`, that's all. So you can use `Single` or `Completable` in your code without manually handle every `CancellationException`.

Unlike [android-disposebag](https://github.com/kizitonwose/android-disposebag), this library doesn't use [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) to listen the lifecycle because the class [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle.html) from [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) doesn't emit states like `PRE_ATTACH`, `ATTACH`, `ACTIVITY_CREATED`, `VIEW_CREATED`, `DESTROY_VIEW`, `SAVE_INSTANCE_STATE`, `DESTROY_VIEW` and `DETACH` while RxUIKotlin does. Furthermore, RxUIKotlin is lightweight, it only require a few dependencies: `appcompat`, `recyclerview`, `rxjava` and `rxandroid`.

### Restrictions

It works only with [RxJava2](https://github.com/ReactiveX/RxJava), it's designed to work with [Kotlin](https://github.com/JetBrains/kotlin) (usage of extensions) and it dispose only the fragments from the [AndroidX library](https://developer.android.com/guide/components/fragments).

## Installation

`implementation 'com.vincentmasselis.rxuikotlin:rxuikotlin-core:1.0.0'`

## Usage

Inside a Fragment :

```kotlin
anObservable
  .subscribe {
    //Do your stuff
  }
  .disposeOnState(FragmentState.DESTROY_VIEW, this)
```

It's exactly the same for activities :
```kotlin
.disposeOnState(ActivityState.DESTROY, this)
```

ViewHolder :
```kotlin
override fun onAttach(detachDisposable: CompositeDisposable) {
  anObservable
    .subscribe {
      //Do your stuff
    }
    .also { detachDisposable.add(it) }
    //.addTo(detachDisposable) with RxKotlin
}
```

That's all !

Unlike `Fragment` and `Activity`, there is no `disposeOnState` extension method available for a `ViewHolder` subclass due to the recycler view library limitation. To continue, you have to subclass `LifecycleAdapter` instead of `RecyclerView.Adapter` and `LifecycleViewHolder` instead of `RecyclerView.ViewHolder`, then, inside your `ViewHolder`, override the method `onAttach(detachDisposable: CompositeDisposable)` and add every of your `Disposable`s into the filled parameter `detachDisposable`.

## Repport an issue

https://github.com/VincentMasselis/RxUIKotlin/issues

## TODO

- Tests, tests, tests !

- Example app
