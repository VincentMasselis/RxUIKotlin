# RxUIKotlin

[![Download](https://api.bintray.com/packages/vincentmasselis/maven/rx-ui-kotlin/images/download.svg) ](https://bintray.com/vincentmasselis/maven/rx-ui-kotlin/_latestVersion)
[![Build Status](https://app.bitrise.io/app/543a61215e5d2cea/status.svg?token=hG0jM55xlaT9IvOgJcyCJA&branch=master)](https://app.bitrise.io/app/543a61215e5d2cea)

Android UI + Kotlin + RxJava2

Made with love at the [Equisense](http://equisense.com) HQ. This library is used in our [Equisense app](https://play.google.com/store/apps/details?id=com.equisense.motions) since a few years.

## Introduction
This library is made to manage your subscriptions when fetching data with RxJava2 inside a `Activity`, a `Fragment` or a `ViewHolder`. It cancel your request depending of the state of the lifecycle by disposing your subscription (calling `dispose()` on the `Disposable` object returned by the `subscribe` methods).

RxUIKotlin is lightweight, the .aar file weights less than 50kb and it only require a few dependencies: `appcompat`, `recyclerview`, `rxjava` and `rxandroid`. RxUIKotlin will never change the behavior of your chaning, it only dipose it when you want to.

RxUIKotlin is splitted into 2 separates artifacts, the `core`, which is explained here, and the [fragmentmanager](https://github.com/VincentMasselis/RxUIKotlin/tree/master/rxuikotlin-fragmentmanager). `fragmentmanager` helps you to listen fragment events, adding or removing from a `FragmentManager`. You can use `fragmentmanager` without `core`, both are independent.

Unlike [RxLifecycle](https://github.com/trello/RxLifecycle), this library doesn't transform your observable by emitting `onComplete()` when your view is gone, it just `dispose()` your `Disposable`, that's all. So you can use `Single` or `Completable` in your code without manually handle every `CancellationException`.

Unlike [android-disposebag](https://github.com/kizitonwose/android-disposebag), this library doesn't use [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) to listen the lifecycle because the class [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle.html) from [Android Architecture](https://developer.android.com/topic/libraries/architecture/index.html) doesn't emit states `PRE_ATTACH`, `ATTACH`, `ACTIVITY_CREATED`, `VIEW_CREATED`, `DESTROY_VIEW`, `SAVE_INSTANCE_STATE`, `DESTROY_VIEW` and `DETACH` while RxUIKotlin does.

### Restrictions

It works only with [RxJava2](https://github.com/ReactiveX/RxJava), it's designed to work with [Kotlin](https://github.com/JetBrains/kotlin) (usage of extensions) and it dispose only the fragments from the [AndroidX library](https://developer.android.com/guide/components/fragments).

## Installation

`implementation "com.vincentmasselis.rxuikotlin:rxuikotlin-core:$rx_ui_kotlin_version"`

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
override fun onAdapterAttach() {
  super.onAdapterAttach()
  anObservable
    .subscribe {
      //Do your stuff
    }
    .disposeOnState(ViewHolderState.ADAPTER_DETACH, this)
}
```

That's all !

## RecyclerView and ViewHolder

Unlike `Fragment` and `Activity`, a `RecyclerView.ViewHolder` doesn't have explicit methods which represents a lifecyle, but, under the hood, there is a way to create a lifecycle-like behavior by using the modified version of `RecyclerView.Adapter` named `LifecycleAdapter` and the modified `RecyclerView.ViewHolder` named `LifecycleViewHolder`.

`LifecycleAdapter` creates 2 lifecycles "Window Attach-Detach" and "Adapter Attach-Detach". First calls `LifecycleViewHolder.onWindowAttach` and `LifecycleViewHolder.onWindowDetach` methods when the `ViewHolder` is dettached from the window and attached again a few moments before `onBindViewHolder` is called. Second calls `LifecycleViewHolder.onAdapterAttach` and `LifecycleViewHolder.onAdapterDetach` when the `LifecycleAdapter` is added or removed from the `RecyclerView`.

To use theses methods, you have to subclass `LifecycleAdapter` instead of `RecyclerView.Adapter` and `LifecycleViewHolder` instead of `RecyclerView.ViewHolder`, then, inside your `ViewHolder`, you can now override theses 4 methods `onWindowAttach`, `onWindowDetach`, `onAdapterAttach` and `onAdapterDetach` to create you own lifecycle behavior just like you do with a `Fragment` or an `Activity`.

```kotlin
override fun onAdapterAttach() {
  super.onAdapterAttach()
  anObservable
    .subscribe {
      //Do your stuff
    }
    .disposeOnState(ViewHolderState.ADAPTER_DETACH, this)
}
```

The only restraint of using `LifecycleAdapter` is the obligation to call `subscribe` like this:
```kotlin
myRecyclerView.subscribe(MyAdapter()).disposeOnState(ActivityState.DESTROY, this)
```

## Repport an issue

https://github.com/VincentMasselis/RxUIKotlin/issues

## TODO

- Example app
