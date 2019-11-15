## FragmentManager

Our examples uses an `Activity`, you can replace this by a `Fragment`, it works the same way.

`rxFragmentsLifecycle()` emits for every fragment lifecycle change.

`rxViewCreatedFragments()` emits a list of fragments which have a view created that mean every fragment after `onCreateView` and before `onDestroyView`.

`rxCreatedFragments()` emits a set of fragments created that mean every fragment between `onCreate` and `onDestroy`.

### Download

`implementation "com.vincentmasselis.rxuikotlin:rxuikotlin-fragmentmanager:$rx_ui_kotlin_version"`

### FragmentManager.rxFragmentsLifecycle()
```kotlin
class MyActivity : AppCompatActivity() {
  // Your code here
  fun onCreate() {
    // Your code here
    supportFragmentManager.rxFragmentsLifecycle(false)
      .subscribe { (state, fragment) ->
        // Every next emission will fire Pair<FragmentState, Fragment>
        // By using this pair, you can remotly listen to the fragments states events
      }
      .disposeOnState(ActivityState.DESTROY, this)
  }
}
```
### FragmentManager.rxCreatedFragments()
```kotlin
class MyActivity : AppCompatActivity() {
  // Your code here
  fun onCreate() {
    // Your code here
    supportFragmentManager.rxCreatedFragments()
      .subscribe { fragmentSet ->
        // Every next emission will fire Set<Fragment>
        // fragmentSet contains a set of created fragments 
      }
      .disposeOnState(ActivityState.DESTROY, this)
  }
}
```
### FragmentManager.rxViewCreatedFragments()
```kotlin
class MyActivity : AppCompatActivity() {
  // Your code here
  fun onCreate() {
    // Your code here
    supportFragmentManager.rxViewCreatedFragments()
      .subscribe { fragmentList ->
        // Every next emission will fire List<Fragment>
        // fragmentList contains a list of fragments which have a view created
      }
      .disposeOnState(ActivityState.DESTROY, this)
  }
}
```
